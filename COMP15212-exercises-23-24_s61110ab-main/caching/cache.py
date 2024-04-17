from memory import Memory
import utilities


# This class does not provide any actual caching and is used as a
# superclass that is extended for the various strategie. It defines
# two variables that should keep track of cache hits and accessors for
# statistics and testing.
#
# cache_hit_count: should be incremented whenever the cache is hit,
# i.e. if the requested element is already in the cache.
#
# cache_hit_flag: should be set to True if the cache was hit on the
# last request and set to False otherwise
#
# Definitions of lookup in subclasses should update these variables as
# appropriate.
class Cache():

    def name(self):
        return "Cache"

    # Takes two parameters. Parameter memory is the "memory". Size is
    # a non-negative integer that indicates the size of the cache.
    def __init__(self, data, size=5):
        self.memory = Memory(data)
        self.cache_hit_count = 0
        self.cache_hit_flag = False

    # Returns information on the number of cache hit counts
    def get_cache_hit_count(self):
        return self.cache_hit_count

    def get_memory_request_count(self):
        return self.memory.get_request_count()

    # Returns the cache hit flag
    def get_cache_hit_flag(self):
        return self.cache_hit_flag

    # Look up an address. Uses caching if appropriate.
    def lookup(self, address):
        return self.memory.lookup(address)


class CyclicCache(Cache):
    def name(self):
        return "Cyclic"

    # TODO: Edit the code below to provide an implementation of a
    # cache that uses a cyclic caching strategy with the given cache
    # size. You can use additional methods and variables as you see
    # fit as long as you provide a suitable overridding of the lookup
    # method.

    def __init__(self, data, size=5):
        super().__init__(data)

        self.cache_save_address = ['_'] * size
        self.cache_save_memory = ['_'] * size
        self.pointer_empty = 0
        self.size = size

    # Look up an address. Uses caching if appropriate.
    def lookup(self, address):

        if address in self.cache_save_address:
            self.cache_hit_count += 1
            self.cache_hit_flag = True
            # to shorten line length, pycodestyle
            ret_val = self.cache_save_address.index(address)
            return self.cache_save_memory[ret_val]
        else:
            self.cache_hit_flag = False

            if (self.pointer_empty == self.size):
                self.pointer_empty = 0

            # eviction
            self.cache_save_memory[self.pointer_empty] = '_'
            self.cache_save_address[self.pointer_empty] = '_'
            # insertion after eviction
            # shorten line length, pycodestyle
            ret_val = self.pointer_empty
            self.cache_save_memory[ret_val] = super().lookup(address)
            self.cache_save_address[ret_val] = address

            self.pointer_empty += 1

            # line length control, pycodestyle
            ret_val = self.cache_save_address.index(address)
            return self.cache_save_memory[ret_val]


class LRUCache(Cache):
    def name(self):
        return "LRU"

    # TODO: Edit the code below to provide an implementation of a
    # cache that uses a least recently used caching strategy with the
    # given cache size.  You can use additional methods and variables
    # as you see fit as long as you provide a suitable overridding of
    # the lookup method.

    def __init__(self, data, size=5):
        super().__init__(data)

        self.cache_save_address = ['_'] * size
        self.cache_save_memory = ['_'] * size
        self.pointer_to_empty = 0
        self.size = size

        self.pointer_LRU = []

    # Look up an address. Uses caching if appropriate.
    def lookup(self, address):

        # handling LRU pointer
        if address in self.pointer_LRU:
            self.pointer_LRU.remove(address)
            self.pointer_LRU.append(address)
        else:
            self.pointer_LRU.append(address)

        # no eviction as it is in the list
        if address in self.cache_save_address:
            self.cache_hit_count += 1
            self.cache_hit_flag = True
            self.pointer_to_empty += 1
            # shorten line length, pycodestyle
            ret_val = self.cache_save_address.index(address)
            return self.cache_save_memory[ret_val]
        else:
            self.cache_hit_flag = False

            # no eviction as lookup < size of cache
            if (self.pointer_to_empty < self.size):
                # shorten line length, pycodestyle
                ret_val = super().lookup(address)
                self.cache_save_memory[self.pointer_to_empty] = ret_val
                self.cache_save_address[self.pointer_to_empty] = address
                self.pointer_to_empty += 1
                # shorten line length, pycodestyle
                ret_val = self.cache_save_address.index(address)
                return self.cache_save_memory[ret_val]
            else:
                # eviction
                # source of problem might be here
                for i in range(len(self.pointer_LRU)):
                    if self.pointer_LRU[i] in self.cache_save_address:
                        # shorten line length, pycodestyle
                        ret = self.pointer_LRU[i]
                        index_of_eviction = self.cache_save_address.index(ret)
                        self.pointer_LRU.pop(i)
                        break
                    index_of_eviction = None

                if index_of_eviction is not None:
                    self.cache_save_memory[index_of_eviction] = '_'
                    self.cache_save_address[index_of_eviction] = '_'
                    # insertion after eviction
                    # shorten line length
                    ret_val = super().lookup(address)
                    self.cache_save_memory[index_of_eviction] = ret_val
                    self.cache_save_address[index_of_eviction] = address

                    self.pointer_to_empty += 1
                    # shorten line length, pycodestyle
                    ret_val = self.cache_save_address.index(address)
                    return self.cache_save_memory[ret_val]
                else:
                    return super().lookup(address)


class MRUCache(Cache):
    def name(self):
        return "MRU"

    # TODO: Edit the code below to provide an implementation of a
    # cache that uses a most recently used eviction strategy with the
    # given cache size.  You can use additional methods and variables
    # as you see fit as long as you provide a suitable overridding of
    # the lookup method.

    def __init__(self, data, size=5):
        super().__init__(data)

        self.cache_save_address = ['_'] * size
        self.cache_save_memory = ['_'] * size
        self.pointer_to_empty = 0
        self.size = size

        self.pointer_MRU = []

    # Look up an address. Uses caching if appropriate.
    def lookup(self, address):

        # handling MRU pointer
        if address in self.pointer_MRU:
            self.pointer_MRU.remove(address)
            self.pointer_MRU.append(address)
        else:
            self.pointer_MRU.append(address)

        # caching
        if address in self.cache_save_address:
            self.cache_hit_count += 1
            self.cache_hit_flag = True
            self.pointer_to_empty += 1
            # shorten line length, pycodestyle
            ret_val = self.cache_save_address.index(address)
            return self.cache_save_memory[ret_val]
        else:
            self.cache_hit_flag = False

            # no eviction as lookup < size of cache
            if (self.pointer_to_empty < self.size):
                # shorten line length, pycodestyle
                ret = super().lookup(address)
                self.cache_save_memory[self.pointer_to_empty] = ret
                self.cache_save_address[self.pointer_to_empty] = address
                self.pointer_to_empty += 1
                # shorten line length, pycodestyle
                ret = self.cache_save_address.index(address)
                return self.cache_save_memory[ret]
            else:
                # eviction
                # shorten line length, pycodestyle
                ret = self.pointer_MRU[len(self.pointer_MRU)-2]
                index_of_eviction = self.cache_save_address.index(ret)
                self.cache_save_memory[index_of_eviction] = '_'
                self.cache_save_address[index_of_eviction] = '_'
                # insertion after eviction
                # shorten line length, pycodestyle
                ret = super().lookup(address)
                self.cache_save_memory[index_of_eviction] = ret
                self.cache_save_address[index_of_eviction] = address

                self.pointer_to_empty += 1
                # shorten line length, pycodestyle
                ret = self.cache_save_address.index(address)
                return self.cache_save_memory[ret]


class LFUCache(Cache):
    def name(self):
        return "LFU"

    # TODO: Edit the code below to provide an implementation of a
    # cache that uses a least frequently used eviction strategy with
    # the given cache size. For this strategy, the cache should keep a
    # count of the number of times an item has been requested. When
    # evicting, the item that is used least frequently should be
    # removed. If there are two items that have the same frequency,
    # then the item that was added *first*, i.e. the item that has
    # been in the cache for the longest time, should be removed. You
    # can use additional methods and variables as you see fit as long
    # as you provide a suitable overridding of the lookup method.

    def __init__(self, data, size=5):
        super().__init__(data)

        self.cache_save_address = ['_'] * size
        self.cache_save_memory = ['_'] * size
        self.pointer_to_empty = 0
        self.size = size

        self.pointer_LFU = []
        self.pointer_LFU_ties = []

    # Look up an address. Uses caching if appropriate.
    def lookup(self, address):

        # handling LFU pointer, always add to pointer to count later
        self.pointer_LFU.append(address)
        # handling LFU pointer if ties
        if address in self.pointer_LFU_ties:
            self.pointer_LFU_ties.remove(address)
            self.pointer_LFU_ties.append(address)
        else:
            self.pointer_LFU_ties.append(address)

        # caching
        if address in self.cache_save_address:
            self.cache_hit_count += 1
            self.cache_hit_flag = True
            self.pointer_to_empty += 1
            # shorten line length, pycodestyle
            ret = self.cache_save_address.index(address)
            return self.cache_save_memory[ret]
        else:
            cache_hit_flag = False

            # no eviction as lookup < size of cache
            if (self.pointer_to_empty < self.size):
                # shorten line length, pycodestyle
                ret = super().lookup(address)
                self.cache_save_memory[self.pointer_to_empty] = ret
                self.cache_save_address[self.pointer_to_empty] = address
                self.pointer_to_empty += 1
                ret = self.cache_save_address.index(address)
                return self.cache_save_memory[ret]
            else:

                # find eviction address
                count_for_eviction = []
                cand_evic = []
                index_of_cand = []
                for i in self.cache_save_address:
                    count_for_eviction.append(self.pointer_LFU.count(i))
                # tie breaker
                if (count_for_eviction.count(min(count_for_eviction)) > 1):
                    # shorten line length, pycodestyle

                    min_index = ([index for index, value in
                                  enumerate(count_for_eviction) if
                                  value == min(count_for_eviction)])

                    for i in min_index:
                        cand_evic.append(self.cache_save_address[i])
                    for i in cand_evic:
                        index_of_cand.append(self.pointer_LFU_ties.index(i))
                    # shorten line length, pycodestyle
                    ret = cand_evic[index_of_cand.index(min(index_of_cand))]
                    index_of_eviction = self.cache_save_address.index(ret)
                else:
                    # shorten line length, pycodestyle
                    ret = min(count_for_eviction)
                    index_of_eviction = count_for_eviction.index(ret)

                # eviction
                self.cache_save_memory[index_of_eviction] = '_'
                self.cache_save_address[index_of_eviction] = '_'
                # insertion after eviction
                # shorten line length, pycodestyle
                ret = super().lookup(address)
                self.cache_save_memory[index_of_eviction] = ret
                self.cache_save_address[index_of_eviction] = address

                self.pointer_to_empty += 1
                # shorten line length, pycodestyle
                ret = self.cache_save_address.index(address)
                return self.cache_save_memory[ret]
