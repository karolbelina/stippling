# stippling

This project is an attempt to create a stippling algorithm for reproducing images as dot patterns. The image is loaded and the pixel information is stored in an array. Then the space is populated with points on a hexagonal lattice. Each point appears as bright as is the pixel array behind it. This produces pleasant-looking patterns as a result of a relatively simple set of calculations.

The project serves mainly as my study and introduction to Clojure, ClojureScript, Figwheel, and Quil.

## Usage

Run `lein figwheel` in your terminal. Wait for a while until you see `Successfully compiled "resources/public/js/main.js"`. Open [localhost:3449](http://localhost:3449) in your browser.
