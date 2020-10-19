# stippling

This project is an attempt to create a stippling algorithm for reproducing images as dot patterns. The image is loaded through a web interface and the pixel information is stored in an array. The space is then populated with points on a hexagonal lattice, and each point appears as bright as is the pixel array behind it. This produces pleasant-looking patterns as a result of a relatively simple set of calculations.

The project serves mainly as my study and introduction to [Clojure](https://clojure.org/), [ClojureScript](https://clojurescript.org/), [Figwheel](https://figwheel.org/), and [Quil](http://quil.info/).

## Usage

Run `lein figwheel` in your terminal. Wait for a while until you see `Successfully compiled "resources/public/js/main.js"`. Open [localhost:3449](http://localhost:3449) in your browser.
