const path = require('path');
const { version } = require('./package');
const webpack = require('webpack');

module.exports = {
    components: 'src/components/**/[A-Z]*.js',
    sections: [
        {
            name: 'Forms',
            components: 'src/components/forms/**/*.js'
        },
        {
            name: 'Layout',
            components: 'src/components/layout/**/*.js'
        },
        {
            name: 'Lists',
            components: 'src/components/lists/**/*.js'
        },
        {
            name: 'Modals',
            components: 'src/components/modals/**/*.js'
        },
        {
            name: 'Pages',
            components: 'src/components/pages/**/*.js'
        },
    ],
    defaultExample: true,
    moduleAliases: {
        'rsg-example': path.resolve(__dirname, 'src'),
    },
    ribbon: {
        url: 'https://github.com/styleguidist/react-styleguidist',
    },
    version,
    webpackConfig: {
        module: {
            rules: [
                {
                    test: /\.jsx?$/,
                    exclude: /node_modules/,
                    loader: 'babel-loader',
                },
                {
                    test: /\.css$/,
                    use: ['style-loader', 'css-loader'],
                },
                {
                    test: /\.png$/,
                    use: ['file-loader'],
                },
            ],
        },
        plugins:[
            new webpack.DefinePlugin({
                process: { env: {} }
            })
        ]
    },
};
