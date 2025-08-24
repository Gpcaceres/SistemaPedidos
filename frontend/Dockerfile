# Stage 1: build the angular app
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: serve the app with nginx
FROM nginx:alpine
COPY --from=build /app/dist/pedido-frontend /usr/share/nginx/html
EXPOSE 80

