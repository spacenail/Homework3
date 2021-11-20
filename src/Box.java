/*
Задача:
1.Даны классы Fruit, Apple extends Fruit, Orange extends Fruit;
2.Класс Box, в который можно складывать фрукты. Коробки условно сортируются по типу фрукта,
 поэтому в одну коробку нельзя сложить и яблоки, и апельсины;
3.Для хранения фруктов внутри коробки можно использовать ArrayList;
4.Сделать метод getWeight(), который высчитывает вес коробки, зная вес одного фрукта
 и их количество: вес яблока – 1.0f, апельсина – 1.5f (единицы измерения не важны);
5.Внутри класса Box сделать метод compare(), который позволяет сравнить текущую коробку
 с той, которую подадут в compare() в качестве параметра. true – если их массы равны,
  false в противоположном случае. Можно сравнивать коробки с яблоками и апельсинами;
6.Написать метод, который позволяет пересыпать фрукты из текущей коробки в другую.
 Помним про сортировку фруктов: нельзя яблоки высыпать в коробку с апельсинами.
  Соответственно, в текущей коробке фруктов не остается, а в другую перекидываются объекты,
  которые были в первой;
Не забываем про метод добавления фрукта в коробку.
 */

import java.util.ArrayList;

public class Box<I extends Fruit> {
    private ArrayList<I> list;

    public Box() {
        list = new ArrayList<>();
    }

    public void add(I item){
        list.add(item);
    }

    public float getWeight(){
        return list.stream().
                reduce(0f,
                        (x,y)-> x + y.getWeight(),
                        (x,y) -> x+y);
    }
    /*
    public float getWeight(){
        float sum = 0f;
        for(I item:list){
            sum+= item.getWeight();
        }
        return sum;
    }
    */

    public boolean isGreather(Box<?> anotherBox){
        return this.getWeight() > anotherBox.getWeight();
    }

    public void pour(Box<I> anotherBox){
        for (I item:list){
            anotherBox.add(item);
        }
        list.clear();
    }
}
