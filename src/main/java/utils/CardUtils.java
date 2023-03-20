package utils;

// utils va adentro de java y se utiliza para crear metodos statics q se puedan usar en all el programa.
public class CardUtils {

    private CardUtils(){};

    //crea (cant) numeros random para usar
    public static String createNumber(Integer cant){
        String newNumber="";
        for (int i = 0; i<cant;i++){
            int newNumber2 = (int) (Math.random() * 10);
            newNumber += String.valueOf(newNumber2);
        }
        return newNumber;
    };

}
