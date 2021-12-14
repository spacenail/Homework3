import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

class MainTest {
private static Main main;

    private static Stream<Arguments> dataForArrayFilter() {
        List<Arguments> testDataList = new ArrayList<>();
        testDataList.add(Arguments.arguments(new int[]{1,2,4,4,2,3,4,1,7},new int[]{1,7}));
        testDataList.add(Arguments.arguments(new int[]{1,2,4,4,2,3,2,1,7},new int[]{2,3,2,1,7}));
        testDataList.add(Arguments.arguments(new int[]{1,2,4,4,2,3,4,5,1,4,7},new int[]{7}));
        testDataList.add(Arguments.arguments(new int[]{1,2,4,4,2,3,4,5,1,4,4},new int[]{}));
        return testDataList.stream();
    }

    @BeforeAll
    public static void setUp() {
        main = new Main();
    }

    @ParameterizedTest
    @MethodSource("dataForArrayFilter")
    public void testArrayFilter(int[] array, int[] resultArray){
        Assertions.assertArrayEquals(resultArray,main.arrayFilter(array));
    }

    @Test
    public void testArrayFilterException(){
        Assertions.assertThrows(RuntimeException.class,
                ()-> main.arrayFilter(new int[]{1,1}));
    }

}