import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        String[] stringArray = {"one","two"};
        Integer[] intArray = {1,2};

        Arrays.stream(stringArray).forEach(s -> System.out.print(s + " "));
        System.out.println();
        swap(stringArray,0,1);
        Arrays.stream(stringArray).forEach(s -> System.out.print(s + " "));
        System.out.println();

        Arrays.stream(intArray).forEach(s -> System.out.print(s + " "));
        System.out.println();
        swap(intArray,0,1);
        Arrays.stream(intArray).forEach(s -> System.out.print(s + " "));
        System.out.println();

        ArrayList newList = toArrayList(stringArray);
        System.out.println(newList.get(0));

        Box<Apple> appleBox = new Box<>();
        appleBox.add(new Apple());
        System.out.println(appleBox.getWeight());

        Box<Apple> anotherAppleBox = new Box<>();
        anotherAppleBox.add(new Apple());
        anotherAppleBox.pour(appleBox);
        System.out.println(appleBox.getWeight());

        Box<Orange> orangeBox = new Box<>();
        orangeBox.add(new Orange());
        System.out.println(orangeBox.getWeight());
        System.out.println(appleBox.compare(orangeBox));

    }


    /*
    Написать метод, который меняет два элемента массива местами (массив может быть любого ссылочного типа);
     */
    public static <T> void swap(T[] array,int ind1,int ind2){
        T temp = array[ind1];
        array[ind1] = array[ind2];
        array[ind2] = temp;
    }

    /*
    Написать метод, который преобразует массив в ArrayList;
     */
    public static <T> ArrayList<T> toArrayList(T[] array){
        return new ArrayList(Arrays.asList(array));
    }
}