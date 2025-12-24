# VibrantCovers - Custom Phone Case E-commerce Store

An e-commerce platform for custom phone cases built with **Spring Boot** (backend) and **Next.js** (frontend).

## Architecture

- **Backend**: Spring Boot 3.2.0 (Java 17+)
  - MySQL database
  - REST API endpoints
  - JWT authentication (Kinde Auth integration)
  - Stripe payment processing
  - UploadThing for image uploads
  - Resend for email notifications

- **Frontend**: Next.js 14.2.3 (React)
  - Server-side rendering
  - Client-side API calls to Spring Boot backend
  - Kinde Auth for user authentication
  - UploadThing React components for file uploads

## Prerequisites

- **Java 17+** (for Spring Boot backend)
- **Node.js 18+** (for Next.js frontend)
- **MySQL 8.0+** (database)
- **Maven** (for building Spring Boot project)

## Setup Instructions

### 1. Database Setup

Create the MySQL database and tables:

```bash
mysql -u root -p < backend/database/schema.sql
```

Or manually run the SQL script in your MySQL client.

### 2. Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Configure database and API keys in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/vibrantcovers
   spring.datasource.username=root
   spring.datasource.password=YOUR_PASSWORD
   
   stripe.secret-key=YOUR_STRIPE_SECRET_KEY
   stripe.webhook-secret=YOUR_STRIPE_WEBHOOK_SECRET
   
   uploadthing.secret-key=YOUR_UPLOADTHING_SECRET
   uploadthing.app-id=YOUR_UPLOADTHING_APP_ID
   
   resend.api-key=YOUR_RESEND_API_KEY
   
   kinde.issuer-url=YOUR_KINDE_ISSUER_URL
   kinde.client-secret=YOUR_KINDE_CLIENT_SECRET
   
   admin.email=YOUR_ADMIN_EMAIL
   app.url=http://localhost:3000
   ```

3. Build and run the Spring Boot application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

### 3. Frontend Setup

1. Navigate to the project root:
   ```bash
   cd ..
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Create `.env.local` file in the project root:
   ```env
   NEXT_PUBLIC_API_URL=http://localhost:8080/api
   
   KINDE_CLIENT_ID=YOUR_KINDE_CLIENT_ID
   KINDE_CLIENT_SECRET=YOUR_KINDE_CLIENT_SECRET
   KINDE_ISSUER_URL=YOUR_KINDE_ISSUER_URL
   KINDE_SITE_URL=http://localhost:3000
   KINDE_POST_LOGOUT_REDIRECT_URL=http://localhost:3000
   KINDE_POST_LOGIN_REDIRECT_URL=http://localhost:3000/auth-callback
   
   ADMIN_EMAIL=YOUR_ADMIN_EMAIL
   
   UPLOADTHING_SECRET=YOUR_UPLOADTHING_SECRET
   UPLOADTHING_APP_ID=YOUR_UPLOADTHING_APP_ID
   ```

4. Start the development server:
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:3000`

## API Endpoints

### Configuration Endpoints
- `GET /api/configurations/{id}` - Get configuration by ID
- `PUT /api/configurations/{id}` - Update configuration

### Upload Endpoints
- `POST /api/upload` - Upload image file
- `POST /api/uploadthing/callback` - UploadThing webhook callback

### Checkout Endpoints
- `POST /api/checkout` - Create Stripe checkout session

### Order Endpoints
- `GET /api/orders/{id}/status` - Get order status
- `PUT /api/orders/{id}/status` - Update order status
- `GET /api/orders/dashboard` - Get dashboard data (admin only)

### Auth Endpoints
- `POST /api/auth/callback` - Create/update user after Kinde authentication

### Webhook Endpoints
- `POST /api/webhooks/stripe` - Stripe webhook handler

## Features

- **Image Upload**: Upload custom images for phone cases via UploadThing
- **Design Customization**: Configure case color, model, material, and finish
- **Payment Processing**: Secure checkout via Stripe
- **Order Management**: Admin dashboard to view and manage orders
- **Email Notifications**: Order confirmation emails via Resend
- **User Authentication**: Secure login/signup via Kinde Auth

## Development

### Backend Development
- Main application: `backend/src/main/java/com/vibrantcovers/VibrantCoversApplication.java`
- Controllers: `backend/src/main/java/com/vibrantcovers/controller/`
- Services: `backend/src/main/java/com/vibrantcovers/service/`
- Entities: `backend/src/main/java/com/vibrantcovers/entity/`

### Frontend Development
- Pages: `src/app/`
- Components: `src/components/`
- API Client: `src/lib/api-client.ts`
- Server Actions: `src/app/*/actions.ts`

## Testing

1. **Upload Flow**: Go to `/configure/upload` and upload an image
2. **Design Flow**: Customize your case on `/configure/design`
3. **Preview Flow**: Review your design on `/configure/preview`
4. **Checkout Flow**: Complete payment via Stripe
5. **Thank You Page**: View order confirmation
6. **Dashboard**: Access admin dashboard at `/dashboard` (admin email required)

## Notes

- The backend uses MySQL (not PostgreSQL)
- Prisma is no longer used; all database operations go through Spring Boot JPA
- Authentication is handled via Kinde Auth with JWT tokens
- Stripe webhooks require a publicly accessible URL (use Stripe CLI for local testing)

## License

MIT
