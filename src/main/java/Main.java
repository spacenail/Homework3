import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        /*
        1. Добавить на серверную сторону чата логирование, с выводом информации о
         действиях на сервере (запущен, произошла ошибка, клиент подключился, клиент
         прислал сообщение/команду).
2. Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный
 массив. Метод должен вернуть новый массив, который получен путем вытаскивания из исходного
 массива элементов, идущих после последней четверки. Входной массив должен содержать
 хотя бы одну четверку, иначе в методе необходимо выбросить RuntimeException.
 Написать набор тестов для этого метода (по 3-4 варианта входных данных).
 Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
3. Написать метод, который проверяет состав массива из чисел 1 и 4. Если в нем нет хоть
одной четверки или единицы, то метод вернет false; Написать набор тестов для этого метода
(по 3-4 варианта входных данных).
         */
    }

    public int[] arrayFilter(int[] array) {
        List<Integer> list = Arrays.stream(array).boxed().collect(Collectors.toList());
        int position = list.lastIndexOf(4);
        if(position == -1){
            throw new RuntimeException("Array does not contain 4");
        }
        return Arrays.copyOfRange(array,position + 1,array.length);
    }
}