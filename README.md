# Setup
## Dev Environment
**Postgres Setup**
1. Start Postgres
   - `make up`
2. Enter Postgres Container
   - `docker exec -it postgres_swen bash`
3. Connect to DB
   - `psql -p 5432 -h localhost -U postgres`
4. Create Database `tour`
   -   `CREATE DATABASE tour;`
5. Grant Privileges to *postgres* user 
   -   `GRANT ALL PRIVILEGES ON DATABASE tour TO postgres;`
   
**Vault Setup**
1. Start vault 
```bash 
   $ vault server --dev --dev-root-token-id="00000000-0000-0000-0000-000000000000"
```
2. Connect to vault and add **KEY**=`api_key_map` **VALUE**=(Map token from API provider)

# Goals
- [ ] GUI using JavaFX
- [ ] Using Presentation-Model Java
- [ ] Layer-based architecture (UI, Business, Data Layer)
- [ ] using design patters? 
- [ ] reusable UI-Components 
- [ ] store tour data and logs in db
- [ ] store images on filesystem
- [ ] generate report
- [ ] 20 unit tests
- [ ] config in config file not in source code
- [ ] document

# Features
- [ ] create new tours
    - a tour consists of:
      - name
      - tour description
      - from
      - to
      - transport type
      - tour distance*
      - estimated time*
      - route information* (image with tour map)
      - \* image, distance and time fetch via API
- [ ] tours are managed in a list and can be (CRUD)
- [ ] a user can create a tour log for a tour
  - multiple tour logs can belong to one tour
  - a tour log consists of:
    - date/time
    - comment
    - difficulty
    - total time
    - rating
- [ ] tour logs are managed in a list and can be (CRUD)
- [ ] user input is validated
- [ ] full text search in tour and tour log data
- [ ] automatically computed tour attributes
  - popularity (based on number of logs)
  - child friendliness (based on difficulty, total times?, distance)
  - full text search also considers the computed values (so when searching this values are used to rank???)
- [ ] import and export of tour data (all tour data or specific tour?)
- [ ] user can generate two types of reports 
  - tour report (contains information of a single tour and all its connected tour logs)
  - summarize report (statistical analysis for each tour provides the average time, distance, and rating over all connected tour logs)
- [ ] unique feature (think about later on!)
