# Build command then run
# docker build --no-cache -t web:0.1.0 .
# Run this command after build
# docker run --env-file ./src/.env --rm --name web-1 -p 169:169 -d web:0.1.0

# dploy source https://youtu.be/EtY36W-PtYU?si=jZTms11YBRjGdF7a

# Build stage
FROM node:20.9.0 as build
# WORKDIR /usr/app
# COPY . /usr/app
# RUN npm ci --force
# RUN npm run build
# COPY package*.json ./
# EXPOSE 8000
WORKDIR /usr/app
COPY . /usr/app 
RUN npm i --force
# Set environment variables
ENV NODE_OPTIONS=--max_old_space_size=3052
ENV API_HOST='http://step-up-kh.duckdns.org:168'

RUN npm run build
# Production stage
FROM nginx:alpine
EXPOSE 169
COPY ./nginx/conf.d/default_react.conf /etc/nginx/conf.d/default_react.conf
# COPY --from=build /usr/app/dist/build /usr/share/nginx/html
COPY --from=build /usr/app/build /usr/share/nginx/html


