# Build stage
FROM node:20.9.0 as build
WORKDIR /usr/app
COPY . /usr/app
RUN npm ci --force
RUN npm run build

# Production stage
FROM nginx:alpine
EXPOSE 169
COPY ./nginx/conf.d/default.conf /etc/nginx/conf.d/default.conf
COPY --from=build /usr/app/dist /usr/share/nginx/html
