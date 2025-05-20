# Build Angular app
FROM node:20 AS build
WORKDIR /app
COPY . .
RUN npm install && npm run build -- --configuration production
RUN ls -la /app/dist/grid-game

# Serve with nginx
FROM nginx:alpine
COPY --from=build /app/dist/grid-game/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
