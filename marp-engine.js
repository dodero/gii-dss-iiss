// An example of engine.js for Marp CLI's custom engine
const { Marp } = require('@marp-team/marp-core')
//const markdownItTableOfContents = require('markdown-it-table-of-contents')

module.exports = {
    engine: opts => new Marp(opts).use(require('markdown-it-table-of-contents'))
}
module.exports = {
    engine: opts => new Marp(opts).use(require('markdown-it-plantuml'))
}