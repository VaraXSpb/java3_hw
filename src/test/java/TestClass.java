import hw6.HomeWork6;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestClass {

    HomeWork6 homeWork6;

    @Before
    public void init() {
        homeWork6 = new HomeWork6();
    }

    @Test
    public void test1() {
        Assert.assertArrayEquals(new int[]{2, 3, 5}, homeWork6.returnAfterFours(new int[]{2, 1, 3, 4, 2, 3, 5}));
    }

    @Test(expected = RuntimeException.class)
    public void test2() {
        Assert.assertArrayEquals(new int[]{2, 3, 5}, homeWork6.returnAfterFours(new int[]{2, 1, 3, 2, 3, 5, 4}));
    }

    @Test
    public void test3() {
        Assert.assertArrayEquals(new int[]{1, 3, 2, 3, 5, 0}, homeWork6.returnAfterFours(new int[]{4, 1, 3, 2, 3, 5, 0}));
    }

    @Test(expected = RuntimeException.class)
    public void test4() {
        Assert.assertArrayEquals(new int[]{1, 3, 2, 3, 5, 0}, homeWork6.returnAfterFours(new int[]{1, 3, 2, 3, 5, 0}));
    }

    @Test
    public void test5() {
        Assert.assertArrayEquals(new int[]{0}, homeWork6.returnAfterFours(new int[]{1, 3, 4, 2, 3, 5, 4, 0}));
    }
}
