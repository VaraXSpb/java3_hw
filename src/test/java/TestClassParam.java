import hw6.HomeWork6;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestClassParam {
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {1, 1, 4, 4, 4},
                {0, 3, 0, 3, 5},
                {1, 0, 3, 2, 4},
                {1, -4, 3, 2, 1},
                {0, 0, 0, 0, 0},
                {0, 5, -1, 5, 6}
        });
    }

    public TestClassParam(int a, int b, int c, int d, int e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }

    private int a;
    private int b;
    private int c;
    private int d;
    private int e;

    HomeWork6 homeWork;

    @Before
    public void init() {
        homeWork = new HomeWork6();
    }

    @Test
    public void test1() {
        if (a == 1) {
            Assert.assertTrue(homeWork.lookForOnesAndFours(new int[]{b, c, d, e}));
        } else if (a == 0) {
            Assert.assertFalse(homeWork.lookForOnesAndFours(new int[]{b, c, d, e}));
        }
    }
}
