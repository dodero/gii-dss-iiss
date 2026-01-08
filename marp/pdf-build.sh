marp --allow-local-files --config-file ./marp/marp-engine.js --pdf --html $1 -o ./pdf/$1.pdf --pdf-outlines --pdf-outlines.pages=false --pdf-outlines.headings=true
open ./pdf/$1.pdf
