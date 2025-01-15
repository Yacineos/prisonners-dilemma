# Use Node.js LTS version
FROM node:18-buster

WORKDIR /app

# Install dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy the rest of the application files
COPY . .

# Expose the development port
EXPOSE 3000

# Start the development server
CMD ["npm", "run", "dev"]
