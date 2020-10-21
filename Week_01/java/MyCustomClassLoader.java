import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class MyCustomClassLoader extends ClassLoader {

    public Class<?> findClass(final String name) throws ClassNotFoundException {
        if ("Hello".equals(name)) {
            byte[] classBytes = getClassBytes();
            return defineClass(name, classBytes, 0, classBytes.length);
        } else {
            return super.findClass(name);
        }
    }

    protected byte[] getClassBytes() {
        byte[] bytes = new byte[1024];
        File file = null;
        try {
            file = new File(MyCustomClassLoader.class.getClassLoader().getResource("Hello.xlass").toURI());
        } catch (URISyntaxException e) {
            return bytes;
        }
        try (FileInputStream inputStream = new FileInputStream(file)) {
            int index = 0;
            while (true) {
                int read = inputStream.read();
                if (read != -1) {
                    bytes[index++] = (byte) (255 - read);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static void main(String[] args) throws Exception {
        MyCustomClassLoader myCustomClassLoader = new MyCustomClassLoader();

        Class<?> customClass = myCustomClassLoader.findClass("Hello");
        if (customClass != null) {
            customClass.getMethod("hello").invoke(customClass.newInstance());
        }
    }
}
