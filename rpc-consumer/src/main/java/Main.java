import com.lyf.serializer.Serializer;
import com.lyf.spi.SpiLoader;

import java.util.Map;

/**
 * @author 17898
 * @version 1.0
 * @date 2024/10/21 16:10
 */
public class Main {
    public static void main(String[] args) {
        Map<String, Class<?>> load = SpiLoader.load(Serializer.class);
        System.out.println(load);
    }
}
