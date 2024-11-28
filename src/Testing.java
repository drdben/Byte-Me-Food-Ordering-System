import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @Test
    void testInvalidLoginAttempts() {
        // Setup;
        System system = new System();

        // Invalid username
        String result1 = system.login("wrongUsername", "validPassword");
        assertEquals("Invalid username or password", result1);

        // Invalid password
        String result2 = system.login("validUsername", "wrongPassword");
        assertEquals("Invalid username or password", result2);

        // Invalid username and password
        String result3 = system.login("wrongUsername", "wrongPassword");
        assertEquals("Invalid username or password", result3);
    }
}
//TESTING NEEDS TO BE DONE