import com.vinsguru.util.JsonUtil;
import com.vinsguru.tests.vendorportal.model.VendorPortalTestData;
import java.util.Arrays;
public class TestDeserialization {
    public static void main(String[] args) {
        VendorPortalTestData[] data = JsonUtil.getTestData("test-data/vendor-portal/invalid-login.json", VendorPortalTestData[].class);
        System.out.println(Arrays.toString(data));
    }
}
