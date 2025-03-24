package lk.ijse.worksphere.util;


import java.util.UUID;

/**
 * Author: Chanuka Prabodha
 * Date: 2025-03-12
 * Time: 02:31 PM
 */

public class IdGenerator {

    public static String generateId(String prefix) {
        String uniquePart = UUID.randomUUID().toString().replace("-", "").substring(0, 10 - prefix.length()).toUpperCase();
        return prefix + uniquePart;
    }
}
