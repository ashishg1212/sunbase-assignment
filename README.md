# sunbase-assignment
A simple Java-based chatbot that uses Retrieval Augmented Generation (RAG) to answer user queries based on a document.

## Instructions
1. Clone the repository and open it in your IDE.
2. Setup a `.env` file and add Supabase URL and API keys.
3. Create a `document.txt` file and add sample text.
4. Provide the path to `document.txt` file in `Chatbot.java`.
5. Run `mvn clean install` to build the project.

## Dependencies
1. Java 8 or above
2. Maven
3. Supabase

## Supabase
1. Create a Database
2. Run the following query:

   ```sql
   -- Create the documents table
   CREATE TABLE documents (
       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
       content TEXT NOT NULL
   );

   -- Enable Row Level Security (RLS) on the table
   ALTER TABLE documents ENABLE ROW LEVEL SECURITY;

   -- Create a policy to allow all users to select data (you can modify this as per your security needs)
   CREATE POLICY "Allow select for all users" 
   ON documents
   FOR SELECT
   USING (true);

   -- Create a policy to allow all users to insert data (you can modify this as per your security needs)
   CREATE POLICY "Allow insert for all users"
   ON documents
   FOR INSERT
   WITH CHECK (true);

   -- Create a policy to allow all users to update data (optional, modify as needed)
   CREATE POLICY "Allow update for all users"
   ON documents
   FOR UPDATE
   USING (true);

   -- Create a policy to allow all users to delete data (optional, modify as needed)
   CREATE POLICY "Allow delete for all users"
   ON documents
   FOR DELETE
   USING (true);
