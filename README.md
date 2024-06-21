## Mini Search Engine

This project involves creating a Little Search Engine that processes a set of text documents. The engine performs the following tasks:

1. **Identify and Index Keywords**: It identifies and indexes keywords, storing them in individual hash tables (`Occurrence`) for each document.
2. **Merge Hash Tables**: These individual hash tables are then merged into a master hash table (`keywordsIndex`) that contains all the keywords.
3. **Search and Retrieve**: The search function can query this master hash table to return the top five most frequently occurring keywords.

This implementation ensures efficient keyword indexing and retrieval from a collection of text documents.
