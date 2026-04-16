package com.example.lizaalertbackend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@Transactional
class LostCaseApiIntegrationTests {

    private MockMvc mockMvc;

    @org.springframework.beans.factory.annotation.Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createsCaseAndReturnsDetails() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/lost-cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "owner": {
                        "fullName": "РђРЅРЅР° РРІР°РЅРѕРІР°",
                        "email": "anna-api@example.com",
                        "phone": "+79990000000",
                        "telegramHandle": "@anna_search"
                      },
                      "pet": {
                        "species": "CAT",
                        "name": "Нюта",
                        "sex": "FEMALE",
                        "primaryColor": "Р‘РµР»С‹Р№",
                        "specialMarks": "Серое пятно на хвосте"
                      },
                      "caseData": {
                        "lostAt": "2026-03-15T08:30:00Z",
                        "lastSeenLocation": {
                          "city": "Москва",
                          "district": "Сокольники",
                          "address": "Русаковская улица, 10"
                        },
                        "circumstances": "Испугалась шума и выбежала из переноски.",
                        "contactPhone": "+79990000000",
                        "rewardDetails": "Р’РѕР·РЅР°РіСЂР°Р¶РґРµРЅРёРµ РіР°СЂР°РЅС‚РёСЂРѕРІР°РЅРѕ",
                        "urgent": true
                      },
                      "media": [
                        {
                          "mediaType": "PHOTO",
                          "externalUrl": "https://example.com/nyuta-1.jpg",
                          "caption": "Основное фото"
                        }
                      ]
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.pet.name").value("Нюта"))
            .andExpect(jsonPath("$.owner.fullName").value("РђРЅРЅР° РРІР°РЅРѕРІР°"))
            .andExpect(jsonPath("$.media[0].externalUrl").value("https://example.com/nyuta-1.jpg"))
            .andReturn();

        String caseId = extractId(result.getResponse().getContentAsString());

        mockMvc.perform(get("/api/v1/lost-cases/{caseId}", caseId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(caseId))
            .andExpect(jsonPath("$.lastSeenLocation.city").value("Москва"));

        mockMvc.perform(get("/api/v1/lost-cases")
                .param("status", "ACTIVE")
                .param("species", "CAT"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(caseId))
            .andExpect(jsonPath("$[0].pet.species").value("CAT"));
    }

    @Test
    void addsSightingAndUpdatesStatus() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/lost-cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "owner": {
                        "fullName": "Павел Сидоров",
                        "email": "pavel-api@example.com",
                        "phone": "+79991112233"
                      },
                      "pet": {
                        "species": "DOG",
                        "name": "Марсель",
                        "sex": "MALE",
                        "primaryColor": "Р С‹Р¶РёР№"
                      },
                      "caseData": {
                        "lostAt": "2026-03-15T09:10:00Z",
                        "circumstances": "Сорвался с поводка возле парка.",
                        "contactPhone": "+79991112233",
                        "urgent": false
                      }
                    }
                    """))
            .andExpect(status().isCreated())
            .andReturn();

        String caseId = extractId(createResult.getResponse().getContentAsString());

        mockMvc.perform(post("/api/v1/lost-cases/{caseId}/sightings", caseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "reporterName": "Свидетель",
                      "reporterPhone": "+79995554433",
                      "seenAt": "2026-03-15T09:25:00Z",
                      "confidenceScore": 85,
                      "location": {
                        "city": "Москва",
                        "district": "Сокольники"
                      },
                      "notes": "Похожую собаку видели у северного входа."
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.sightings[0].reporterName").value("Свидетель"));

        mockMvc.perform(patch("/api/v1/lost-cases/{caseId}/status", caseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "status": "FOUND"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("FOUND"));
    }

    @Test
    void rejectsDuplicateOwnerEmail() throws Exception {
        String payload = """
            {
              "owner": {
                "fullName": "Мария Петрова",
                "email": "duplicate@example.com",
                "phone": "+79998887766"
              },
              "pet": {
                "species": "BIRD",
                "name": "РљРµС€Р°",
                "sex": "UNKNOWN",
                "primaryColor": "Р—РµР»С‘РЅС‹Р№"
              },
              "caseData": {
                "lostAt": "2026-03-15T10:15:00Z",
                "circumstances": "Р’С‹Р»РµС‚РµР» РІ РѕРєРЅРѕ.",
                "contactPhone": "+79998887766",
                "urgent": true
              }
            }
            """;

        mockMvc.perform(post("/api/v1/lost-cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/lost-cases")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value("Пользователь с таким email уже существует"));
    }

    private String extractId(String content) {
        Matcher matcher = Pattern.compile("\"id\":\"([^\"]+)\"").matcher(content);
        if (!matcher.find()) {
            throw new IllegalStateException("Не удалось извлечь id из ответа: " + content);
        }
        return matcher.group(1);
    }
}

