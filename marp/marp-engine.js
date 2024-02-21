// An example of engine.js for Marp CLI's custom engine
const { Marp } = require('@marp-team/marp-core')
//const markdownItTableOfContents = require('markdown-it-table-of-contents')
const markdownItPlantUml = require('markdown-it-plantuml')
//const markdownItMermaid = require('markdown-it-mermaid')

module.exports = {
    engine: opts => new Marp(opts).use(require('markdown-it-table-of-contents'))
        .use(markdownItPlantUml)
        //.use(markdownItKroki)
        //.use(markdownItMermaid)
}

// module.exports = {
//     //inputDir: './slides',
//     engine: ({ marp }) => marp.use(require('@kazumatu981/markdown-it-kroki'), {
//         entrypoint: "https://kroki.io",
//     })
// }