# Stage 1: Build an Angular Docker Image
FROM node:16 as build
WORKDIR /app
COPY package*.json /app/
RUN npm install
COPY . /app
RUN npm run build -- --output-path=./dist/out --configuration production --base-href=/a.hartl1/music-voting/
# Stage 2, use the compiled app, ready for production with Nginx
FROM nginx
COPY --from=build /app/dist/out/ /usr/share/nginx/html
COPY custom-nginx.conf /etc/nginx/conf.d/custom-nginx.conf
