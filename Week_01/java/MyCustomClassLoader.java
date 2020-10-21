import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MyCustomClassLoader extends ClassLoader {

    public Class<?> findClass(final String name) throws ClassNotFoundException {
        if ("Hello".equals(name)) {
            try {
                ClassByteData classByteData = getCustomClassByteData();
                return defineClass(name, classByteData.bytes, 0, classByteData.length);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return super.findClass(name);
    }

    private ClassByteData getCustomClassByteData() throws URISyntaxException, IOException {
        ClassByteData classByteData = new ClassByteData();
        File file = new File(getCustomClassPath());
        try (FileInputStream inputStream = new FileInputStream(file)) {
            int index = 0;
            byte[] bytes = new byte[1024];
            while (true) {
                int read = inputStream.read();
                if (read != -1) {
                    bytes[index++] = (byte) (255 - read);
                } else {
                    break;
                }
            }
            classByteData.bytes = bytes;
            classByteData.length = index;
        }
        return classByteData;
    }

    private URI getCustomClassPath() throws URISyntaxException {
        return MyCustomClassLoader.class.getClassLoader().getResource("Hello.xlass").toURI();
    }

    class ClassByteData {
        private byte[] bytes;
        private int length;
    }

    public static void main(String[] args) throws Exception {
        MyCustomClassLoader myCustomClassLoader = new MyCustomClassLoader();

        Class<?> customClass = myCustomClassLoader.findClass("Hello");
        if (customClass != null) {
            customClass.getMethod("hello").invoke(customClass.newInstance());
        }
    }
}
