from memory import Memory
from cache import Cache, CyclicCache, LRUCache, MRUCache, LFUCache
import utilities
import unittest

# A collection of basic unit tests for caching. These tests will check
# some basic aspects of the implementation, but should not be
# considered comprehensive in any way.

# Stop unittest printing traces out. Uncomment this line if you want
# to see trace information
__unittest = True


class BasicTestCase(unittest.TestCase):

    def setUp(self):
        data = utilities.sample_data(size=100)
        # This test suite assumes that the cache is of size
        # 5. Changing this may result in some tests failing.
        self.default_cache = Cache(data)
        self.cyclic = CyclicCache(data)
        self.lru = LRUCache(data)
        self.mru = MRUCache(data)
        self.lfu = LFUCache(data)


# Unit tests
class TestCaseNull(BasicTestCase):

    # Simple lookup.
    def lookup_check(self, impl, location):
        # Lookup location. Should be non null.
        datum_1 = impl.lookup(location)
        self.assertTrue(datum_1)

    # Memory
    def test_default_cache(self):
        self.lookup_check(self.default_cache, 0)

    # Cyclic
    def test_cyclic(self):
        self.lookup_check(self.cyclic, 0)

    # LRU
    def test_lru(self):
        self.lookup_check(self.lru, 0)

    # MRU
    def test_mru(self):
        self.lookup_check(self.mru, 0)

    # LFU
    def test_lfu(self):
        self.lookup_check(self.lfu, 0)


# Unit tests
class TestCaseFlag(BasicTestCase):

    # Simple lookup.
    def lookup_check(self, impl, location, expected):
        # Lookup location.
        datum_1 = impl.lookup(location)
        # Look up again
        datum_2 = impl.lookup(location)
        # Results should be equal
        self.assertEqual(datum_1, datum_2, "Lookup results don't match")
        # Check whether it hit the cache if expected.
        self.assertEqual(impl.get_cache_hit_flag(),
                         expected,
                         f"Expected flag {expected}")

    # Memory
    def test_default_cache(self):
        self.lookup_check(self.default_cache, 0, False)

    # Cyclic
    def test_cyclic(self):
        self.lookup_check(self.cyclic, 0, True)

    # LRU
    def test_lru(self):
        self.lookup_check(self.lru, 0, True)

    # MRU
    def test_mru(self):
        self.lookup_check(self.mru, 0, True)

    # LFU
    def test_lfu(self):
        self.lookup_check(self.lfu, 0, True)


class TestCaseLookup(BasicTestCase):

    # Simple lookup comparisons. The results of looking up a location
    # twice should be the same
    def lookup_check(self, impl, location):
        # Lookup location. Should be non null.
        datum_1 = impl.lookup(location)
        self.assertTrue(datum_1)

        # Lookup location again. Should still be non null.
        datum_2 = impl.lookup(location)
        self.assertTrue(datum_2)

        # The two results should be equal
        self.assertEqual(datum_1, datum_2, "Lookup results don't match")

    # Memory
    def test_default_cache(self):
        self.lookup_check(self.default_cache, 0)
        self.lookup_check(self.default_cache, 10)

    # Cyclic
    def test_cyclic(self):
        self.lookup_check(self.cyclic, 0)
        self.lookup_check(self.cyclic, 10)

    # LRU
    def test_lru(self):
        self.lookup_check(self.lru, 0)
        self.lookup_check(self.lru, 10)

    # MRU
    def test_mru(self):
        self.lookup_check(self.mru, 0)
        self.lookup_check(self.mru, 10)

    # LFU
    def test_lfu(self):
        self.lookup_check(self.lfu, 0)
        self.lookup_check(self.lfu, 10)


class TestCaseMemoryHit(BasicTestCase):

    # Lookup the same location twice and check the hit count
    def caching_check(self, impl, diff):
        # Warm up and fill the cache.
        for loc in range(10, 20):
            impl.lookup(loc)

        # Lookup location 1
        datum_1 = impl.lookup(1)
        hits_1 = impl.get_memory_request_count()

        # Lookup location 1
        datum_2 = impl.lookup(1)
        hits_2 = impl.get_memory_request_count()
        # If the cache is working, then the hit count should have
        # changed by the given amount
        self.assertEqual(hits_1+diff, hits_2, "Memory hit count incorrect")

    # Default_cache. Hit count should increase by 1
    def test_default_cache(self):
        self.caching_check(self.default_cache, 1)

    # Cyclic. Hit count should not increase as the cache should be used.
    def test_cyclic(self):
        self.caching_check(self.cyclic, 0)

    # LRU. Hit count should not increase as the cache should be used
    def test_lru(self):
        self.caching_check(self.lru, 0)

    # MRU. Hit count should not increase as the cache should be used
    def test_mru(self):
        self.caching_check(self.mru, 0)

    # LFU. Hit count should not increase as the cache should be used
    def test_lfu(self):
        self.caching_check(self.lfu, 0)


class TestCaseCacheHit(BasicTestCase):

    # Lookup the same location twice and check the cache hit count
    def caching_check(self, impl, diff):
        # Warm up and fill the cache.
        for loc in range(10, 20):
            impl.lookup(loc)

        # Lookup location 1
        datum_1 = impl.lookup(1)
        hits_1 = impl.get_cache_hit_count()

        # Lookup location 1
        datum_2 = impl.lookup(1)
        hits_2 = impl.get_cache_hit_count()
        # If the cache is working, then the hit count should have
        # changed by the given amount
        self.assertEqual(hits_1+diff, hits_2, "Cache hit count incorrect")

    # Default_cache. Cache hit count should not increase as the cache
    # should be used.
    def test_default_cache(self):
        self.caching_check(self.default_cache, 0)

    # Cyclic. Cache hit count should increase as the cache should be used.
    def test_cyclic(self):
        self.caching_check(self.cyclic, 1)

    # LRU. Cache hit count should increase as the cache should be used
    def test_lru(self):
        self.caching_check(self.lru, 1)

    # MRU. Cache hit count should increase as the cache should be used
    def test_mru(self):
        self.caching_check(self.mru, 1)

    # LFU. Cache hit count should increase as the cache should be used
    def test_lfu(self):
        self.caching_check(self.lfu, 1)


class TestCaseMultipleLookup(BasicTestCase):

    # Here we look up five items, then request the first one again.
    # Then check the hit counts.
    def caching_check(self, impl, diff):
        # Warm up and fill the cache.
        for loc in range(10, 20):
            impl.lookup(loc)

        # Look up 5 items
        datum_1 = impl.lookup(1)
        datum_2 = impl.lookup(2)
        datum_3 = impl.lookup(3)
        datum_4 = impl.lookup(4)
        datum_5 = impl.lookup(5)
        hits_1 = impl.get_memory_request_count()
        datum_6 = impl.lookup(1)
        hits_2 = impl.get_memory_request_count()
        # Data should be the same
        self.assertEqual(datum_1, datum_6, "Lookup values don't match")
        # If the cache is working, then the hit count should have
        # changed by the given amount
        self.assertEqual(hits_1+diff, hits_2, "Memory hit count incorrect")

    # Default_cache. Hit count should increase by 1
    def test_default_cache(self):
        self.caching_check(self.default_cache, 1)

    # Cyclic. Hit count should not increase as the cache should be used.
    def test_cyclic(self):
        self.caching_check(self.cyclic, 0)

    # LRU. Hit count should not increase as the cache should be used.
    def test_lru(self):
        self.caching_check(self.lru, 0)

    # MRU. Note that hit count *will* increase as address 1 will have
    # been evicted following the lookup of address 2.
    def test_mru(self):
        self.caching_check(self.mru, 1)

    # LFU. Hit count should not increase as the cache should be used.
    def test_lfu(self):
        self.caching_check(self.lfu, 0)


def suite():
    suite = unittest.TestSuite()
    suite.addTest(TestCaseNull('test_default_cache'))
    suite.addTest(TestCaseNull('test_cyclic'))
    suite.addTest(TestCaseNull('test_lru'))
    suite.addTest(TestCaseNull('test_mru'))
    suite.addTest(TestCaseNull('test_lfu'))
    suite.addTest(TestCaseFlag('test_default_cache'))
    suite.addTest(TestCaseFlag('test_cyclic'))
    suite.addTest(TestCaseFlag('test_lru'))
    suite.addTest(TestCaseFlag('test_mru'))
    suite.addTest(TestCaseFlag('test_lfu'))
    suite.addTest(TestCaseLookup('test_default_cache'))
    suite.addTest(TestCaseLookup('test_cyclic'))
    suite.addTest(TestCaseLookup('test_lru'))
    suite.addTest(TestCaseLookup('test_mru'))
    suite.addTest(TestCaseLookup('test_lfu'))
    suite.addTest(TestCaseMemoryHit('test_default_cache'))
    suite.addTest(TestCaseMemoryHit('test_cyclic'))
    suite.addTest(TestCaseMemoryHit('test_lru'))
    suite.addTest(TestCaseMemoryHit('test_mru'))
    suite.addTest(TestCaseMemoryHit('test_lfu'))
    suite.addTest(TestCaseCacheHit('test_default_cache'))
    suite.addTest(TestCaseCacheHit('test_cyclic'))
    suite.addTest(TestCaseCacheHit('test_lru'))
    suite.addTest(TestCaseCacheHit('test_mru'))
    suite.addTest(TestCaseCacheHit('test_lfu'))
    suite.addTest(TestCaseMultipleLookup('test_default_cache'))
    suite.addTest(TestCaseMultipleLookup('test_cyclic'))
    suite.addTest(TestCaseMultipleLookup('test_lru'))
    suite.addTest(TestCaseMultipleLookup('test_mru'))
    suite.addTest(TestCaseMultipleLookup('test_lfu'))
    return suite


if __name__ == '__main__':
    runner = unittest.TextTestRunner()
    runner.run(suite())
