FROM node:14.3.0-alpine AS builder

COPY . ./application
WORKDIR /application

RUN npm i
RUN $(npm bin)/ng build --prod

FROM nginx:1.17.10-alpine
COPY --from=builder /application/dist/orquest/ /usr/share/nginx/html
