# CITRUS Delivery Record

- Change set: `vps-swap`
- Goal: add persistent swap space on the target VPS to reduce OOM risk during builds and application startup

## Environment

- Host: `185.21.8.116:2222`
- OS: `Ubuntu 24.04.3 LTS`

## Actions performed

- inspected current memory and disk state
- created `/swapfile` with size `2G`
- set permissions to `600`
- initialized swap with `mkswap`
- enabled swap with `swapon`
- added `/swapfile` entry to `/etc/fstab`
- tuned:
  - `vm.swappiness=10`
  - `vm.vfs_cache_pressure=50`

## Validation

- `swapon --show`
- `free -h`
- `grep '^/swapfile ' /etc/fstab`
- `sysctl vm.swappiness vm.vfs_cache_pressure`

## Outcome

- swap is active and available to the system
- swap is configured to persist after reboot
- current VPS now has `2G` swap on top of `~961MiB` RAM
