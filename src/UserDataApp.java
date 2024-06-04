import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class UserDataApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите данные в формате: Фамилия Имя Отчество датарождения номертелефона пол");
        String input = scanner.nextLine();

        String[] userData = input.split(" ");

        try {
            if (userData.length != 6) {
                throw new IllegalArgumentException("Неверное количество данных. Ожидалось 6, получено " + userData.length);
            }

            String lastName = userData[0];
            String firstName = userData[1];
            String middleName = userData[2];
            String birthDate = userData[3];
            String phoneNumber = userData[4];
            String gender = userData[5];

            // Validate birthDate
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate parsedBirthDate;
            try {
                parsedBirthDate = LocalDate.parse(birthDate, dateFormatter);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Неверный формат даты рождения. Ожидался формат dd.MM.yyyy", e);
            }

            // Validate phoneNumber
            long parsedPhoneNumber;
            try {
                parsedPhoneNumber = Long.parseLong(phoneNumber);
                if (parsedPhoneNumber <= 0) {
                    throw new IllegalArgumentException("Номер телефона должен быть положительным числом.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Неверный формат номера телефона. Ожидалось целое беззнаковое число.", e);
            }

            // Validate gender
            if (!(gender.equals("f") || gender.equals("m"))) {
                throw new IllegalArgumentException("Неверный формат пола. Ожидался символ f или m.");
            }

            String userRecord = String.join(" ", lastName, firstName, middleName, birthDate, phoneNumber, gender);
            writeToFile(lastName, userRecord);

            System.out.println("Данные успешно сохранены.");

        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void writeToFile(String lastName, String userRecord) throws IOException {
        String fileName = lastName + ".txt";
        Path filePath = Paths.get(fileName);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(userRecord);
            writer.newLine();
        } catch (IOException e) {
            throw new IOException("Ошибка при записи в файл " + fileName, e);
        }
    }
}
