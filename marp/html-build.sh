# cp -R img/ html/img/
# marp --allow-local-files --config-file ./marp/marp-engine.js --html $1.md -o html/$1.html
cp -R `dirname $1`/img/ ./html/`dirname $1`/img/
marp --allow-local-files --config-file ./marp/marp-engine.js --html $1 -o ./html/$1.html
open ./html/$1.html