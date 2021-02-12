import java.io.FileNotFoundException;

@SuppressWarnings({"unused", "UnusedAssignment"})
public class MatrixMain {
    public static void main(String[] args) {
        if (args.length < 1)
            System.out.println("Usage: matrix <input file path> [output file path (not implemented yet]");
        String inputFilePath, outputFilePath;
        inputFilePath = getAbsolutePath(args[0]);
        if (args.length > 1) outputFilePath = getAbsolutePath(args[1]);
        System.out.println(inputFilePath);
        try {
            Matrix matrix = new Matrix(inputFilePath);
            matrix.print();
            System.out.println();
            matrix.simpleInsertSort();
            matrix.print();
            System.out.println();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получить абсолютный путь к указанному файлу или директории в виде строки.
     * Если путь начинается с '/' или с '\', то он считается абсолютным и возвращается неизменным,
     * в противном случае он считается относительным для текущей рабочей директории.
     *
     * @param path путь для парса
     * @return абсолютный путь
     */
    private static String getAbsolutePath(String path) {
        if (path.startsWith("/") || path.startsWith("\\")) {
            path = path.replace('/', '\\');
            return path;
        } else return System.getProperty("user.dir") + "\\" + path;
    }
}