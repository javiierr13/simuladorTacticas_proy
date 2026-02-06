FROM node:22-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

# Exponemos el puerto 4200 dentro del contenedor
EXPOSE 4200

# Ejecutamos Angular en modo desarrollo accesible desde fuera del contenedor
CMD ["npm", "start", "--", "--host", "0.0.0.0", "--port", "4200"]

