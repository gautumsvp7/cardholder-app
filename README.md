# Card information app 
## How to run 
1. Ensure you have Java and Maven installed.

2. Navigate to the project's root directory in your terminal.

3. Run the application using the command: mvn spring-boot:run

4. Access the user interface in your browser at: http://localhost:8080


## Database Design:

The application is built around the table card_details, with id as the primary key.
The design ensures that the full, unencrypted card number exists only temporarily in memory during processing. For storage, the card number is immediately encrypted using AES and saved to the ENCRYPTED_PAN column. This is the core security measure, preventing exposure of sensitive data even if the database itself is compromised.

To enable efficient searching without weakening security, only the last four digits of the PAN are stored in a separate, plaintext column named PAN_LAST_FOUR. This is critical for performance, as searching on an encrypted field would require decrypting every record in the database, which is not scalable.

Other columns in the table include ID as the primary key, CARDHOLDER_NAME for the user's name, and an automatically generated CREATED_AT timestamp for record-keeping.
