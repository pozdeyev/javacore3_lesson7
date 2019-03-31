
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Java. Level 3. Lesson 7.
 * @version 31.03.2019
 */

/*
Создать класс, который может выполнять «тесты». В качестве тестов выступают классы с наборами методов
с аннотациями @Test. Для этого у него должен быть статический метод start(), которому в качестве параметра передается
или объект типа Class, или имя класса. Из «класса-теста» вначале должен быть запущен метод с аннотацией @BeforeSuite,
если такой имеется. Далее запущены методы с аннотациями @Test, а по завершении всех тестов – метод с
аннотацией @AfterSuite. К каждому тесту необходимо добавить приоритеты (int числа от 1 до 10),
в соответствии с которыми будет выбираться порядок их выполнения. Если приоритет одинаковый,
то порядок не имеет значения. Методы с аннотациями @BeforeSuite и @AfterSuite должны присутствовать
в единственном экземпляре, иначе необходимо бросить RuntimeException при запуске «тестирования».

*/

public class lesson7_reflectAPI {

    public static void main(String[] args) throws Exception {

        Class clazz = TestingClass.class;

        Object testObj = clazz.newInstance();
        Method[] methods = clazz.getDeclaredMethods(); //возвращаем методы в виде массива


        ArrayList<Method> al = new ArrayList<>(); //коллекция

        Method beforeMethod = null; //инициализируем
        Method afterMethod = null;

        // Пробегаем по всем методам класса

        for (Method o : clazz.getDeclaredMethods()) {
            if (o.isAnnotationPresent(Test.class)) {
                al.add(o); //добавляем в коллекцию
            }

            if (o.isAnnotationPresent(BeforeSuite.class)) {
                if (beforeMethod == null) beforeMethod = o;
                else throw new RuntimeException("More than one annotation method BeforeSuite"); //если существует еще
            }

            if (o.isAnnotationPresent(AfterSuite.class)) {
                if (afterMethod == null) afterMethod = o;
                else throw new RuntimeException("More than one annotation method AfterSuite");
            }

            al.sort(new Comparator<Method>() {
                @Override //переопределяем метод базового класса
                public int compare(Method o1, Method o2) {
                    return o2.getAnnotation(Test.class).priority()-o1.getAnnotation(Test.class).priority();
                }
            });
        }


        if (beforeMethod != null) beforeMethod.invoke(testObj, null); //"Будим" метод с аннотацией beforeSuite
        for (Method o : al) o.invoke(testObj, null); //"Будим"  методы в соответствии с приоритетом
        if (afterMethod != null) afterMethod.invoke(testObj, null); //"Будим" метод с аннотацией afterSuite
    }

}
