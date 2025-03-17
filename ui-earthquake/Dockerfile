# React uygulamasını build et
FROM node:18 AS builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Production için Nginx kullan
FROM nginx:alpine
COPY --from=builder /app/build /usr/share/nginx/html

# Varsayılan olarak Nginx 80 portunda çalışır
CMD ["nginx", "-g", "daemon off;"]
