# Simple Java HTTP Server
 
A minimal, dependency-free HTTP server written in plain Java. Serves static HTML files from a local directory over raw sockets — no frameworks, no external libraries.
 
## Features
 
- Handles `GET` requests over a `ServerSocket`
- Serves static files from a configurable root directory
- Defaults to `index.html` for requests to `/`
- Returns a `404 Not Found` page for missing files
## Prerequisites
 
- Java 11+
## Setup
 
### 1. Clone the repository
 
```bash
git clone https://github.com/<your-username>/<your-repo>.git
cd <your-repo>
```
 
### 2. Add your site files
 
Create a `www` directory (or update `ROOT_DIRECTORY` to point elsewhere) and add an `index.html`:
 
```bash
mkdir www
echo "<h1>Hello, world!</h1>" > www/index.html
```
 
## Usage
 
Compile and run:
 
```bash
javac HTTPServer.java
java HTTPServer
```
 
The server starts on port `18080` by default. Visit:
 
```
http://localhost:18080/
```
 
### Example console output
 
```
Opening the server socket on port 18080
Server waiting for client...
Client connected!
Request: GET / HTTP/1.1
Server waiting for client...
```
 
## Configuration
 
Both values are constants at the top of `HTTPServer.java`:
 
| Constant | Default | Description |
|---|---|---|
| `PORT` | `18080` | Port the server listens on |
| `ROOT_DIRECTORY` | `./www` | Directory static files are served from |
 
## Known Limitations
 
- **No path traversal protection.** `requestedFile` is concatenated directly into a filesystem path without sanitization, so a request like `GET /../../../../etc/passwd` could potentially read files outside `ROOT_DIRECTORY`. Validate/normalize the resolved path before serving if this will be exposed beyond local/trusted use.
- **Single-threaded.** Each request is handled sequentially on the main thread — one slow client blocks all others. Consider a thread pool (`ExecutorService`) per connection for concurrent handling.
- **Only `GET` is supported.** Any other method is silently ignored.
- **Only `text/html` is served**, regardless of the actual file type (no MIME type detection for CSS, JS, images, etc.).
- **No request size or timeout limits**, making it easy to hang the server with a malformed or slow client.

## Roadmap Ideas
 
- [ ] Sanitize and canonicalize requested paths to prevent traversal
- [ ] Add a thread pool for concurrent request handling
- [ ] Detect MIME types based on file extension
- [ ] Support `HEAD` and basic `POST` handling
- [ ] Add configurable logging levels

