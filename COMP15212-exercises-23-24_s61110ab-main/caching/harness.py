import sys
import argparse
import utilities
import logging
from memory import Memory
from cache import Cache, CyclicCache, LRUCache, MRUCache, LFUCache

# ANSI Colours for nice display


class bcolours:
    BLACK = '\033[90m'
    RED = '\033[91m'
    GREEN = '\033[92m'
    YELLOW = '\033[93m'
    BLUE = '\033[94m'
    MAGENTA = '\033[95m'
    CYAN = '\033[96m'
    WHITE = '\033[97m'
    RESET = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('-s', '--strategy',
                        help='Expects one of None (default), LRU, MRU or LFU',
                        default="None")
    parser.add_argument('-l', '--log-level', default='WARNING',
                        help='set log level')
    args = parser.parse_args()

    try:
        logging.basicConfig(level=args.log_level)
    except ValueError:
        logging.error("Invalid log level: {}".format(args.log_level))
        sys.exit(1)

    logger = logging.getLogger(__name__)
    logger.info("Log level set: {}".format(logging.getLevelName(
        logger.getEffectiveLevel())))

    model = None
    # Create some memory of size 10.
    data = utilities.sample_data(size=10)

    if args.strategy == "None":
        model = Cache(data)
    elif args.strategy == "Cyclic":
        model = CyclicCache(data)
    elif args.strategy == "LRU":
        model = LRUCache(data)
    elif args.strategy == "MRU":
        model = MRUCache(data)
    elif args.strategy == "LFU":
        model = LFUCache(data)
    else:
        print("Unknown strategy: {}".format(args.strategy))
        sys.exit(1)

    # Reads a list of integers from the command line. No error
    # checking, so non integers will bomb out.
    count = 0
    location = sys.stdin.readline().strip()
    while (location):
        count += 1
        location = int(location)
        print("{}{:03d}{},{}{:2d}{}, {}{}{}".format(bcolours.GREEN,
                                                    count,
                                                    bcolours.RESET,
                                                    bcolours.BLUE,
                                                    location,
                                                    bcolours.RESET,
                                                    bcolours.RED,
                                                    model.lookup(location),
                                                    bcolours.RESET))
        location = sys.stdin.readline().strip()
    print(f"Model: {bcolours.BLACK}{model.name()}{bcolours.RESET}")
    print(f"{bcolours.YELLOW}{count} Accesses{bcolours.RESET}")
    print(f"{bcolours.YELLOW}{model.get_memory_request_count()}\
 Memory Hits{bcolours.RESET}")
    print(f"{bcolours.YELLOW}{model.get_cache_hit_count()}\
 Cache Hits{bcolours.RESET}")
