# Comics Open — GitHub + Vercel + Spring Boot (Java 11)

## Architecture
- `frontend/`: HTML/CSS/JS deployed on **Vercel**
- `backend/`: Java 11 / Spring Boot 2.7.18 REST API deployed as **Render Docker Web Service**
- PostgreSQL for hosted persistence; local H2 file DB for local testing.
- Vercel rewrites `/api/*` to Render, keeping same-origin fetch and session cookies.
- Translation proxy expects a **separately hosted** FastAPI OCR service. Do not set `FASTAPI_BASE_URL=localhost` in production.

## 1. Local test (no domain, no GitHub needed)
Install Maven 3.9.x (portable ZIP works without administrator privileges). In PowerShell:

```powershell
cd backend
mvn spring-boot:run
```
Open `http://localhost:8080/api/csrf` to test backend (it returns a token). For the frontend, run a local static server in `frontend` (`python -m http.server 5500`) but **API requests won't work** until proxied; easiest full local integration is to copy `frontend` files into `backend/src/main/resources/static` temporarily and visit `http://localhost:8080`.

## 2. GitHub
Create a repository named `comics-open`. Upload **the contents of this folder** (frontend, backend, README, .gitignore), not the ZIP. Never upload `.env`, credentials, database files, API keys or service-account JSON.

## 3. Render backend
- Create a PostgreSQL database (check plan costs and retention).
- Create a Render **Web Service** connected to the same GitHub repository.
- Set Root Directory to `backend`, Language/Runtime to **Docker**.
- Environment: `DB_DRIVER=org.postgresql.Driver`, `DATABASE_URL=jdbc:postgresql://...`, `DB_USERNAME`, `DB_PASSWORD`, `COOKIE_SECURE=true`.
- The Java service reads Render's `PORT` automatically. Confirm `https://YOUR-SERVICE.onrender.com/api/csrf` responds.
- Database URL must be **JDBC format**, not `postgresql://` URI. Set database credentials as private Render environment variables.

## 4. Vercel frontend
- Edit `frontend/vercel.json`: replace `https://REPLACE_WITH_YOUR_RENDER_SERVICE.onrender.com` with the actual HTTPS Render backend URL (no trailing slash).
- Commit and push the change to GitHub.
- Vercel > Add New Project > Import your GitHub repository.
- Root Directory: `frontend`. Framework Preset: **Other**. Build Command: blank; Output Directory: `.` if requested.
- Deploy; Vercel provides a `*.vercel.app` URL. No paid custom domain required.
- Verify `/api/csrf`, signup, login, posting and logout on the Vercel URL.

## 5. Known limits / security
- Spring Boot 2.7 is out of support; Java 11 compatibility is for coursework. Upgrade for long-term public production use.
- Session cookies and CSRF are used; backend should be reached through the same-origin Vercel `/api` proxy.
- For public production use add rate limiting, moderation, email verification, stronger operational monitoring and backups.
- Hosting and database may have charges, quotas, sleep policies or data expiration.
- No account deployment or GitHub upload is performed by this ZIP.
- OCR requires a reachable separately deployed FastAPI endpoint and configured cloud credentials; otherwise only community/auth work.
