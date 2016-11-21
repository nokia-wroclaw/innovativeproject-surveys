var path = require('path');
var sassLoader;
var cssLoader;
var fileLoader = 'file-loader?name=[path][name].[ext]';
var htmlLoader = [
    'html-loader'
].join('!');
var jsonLoader = ['json-loader'];

var sassParams = [
    'outputStyle=expanded',
    'includePaths[]=' + path.resolve(__dirname, '../scss'),
    'includePaths[]=' + path.resolve(__dirname, '../node_modules')
];

var jsxLoader = [];

sassParams.push('sourceMap', 'sourceMapContents=true');
sassLoader = [
    'style-loader',
    'css-loader?sourceMap',
    'postcss-loader',
    'sass-loader?' + sassParams.join('&')
].join('!');

cssLoader = [
    'style-loader',
    'css-loader?sourceMap',
    'postcss-loader'
].join('!');

var exclude = /node_modules.(?!vis)|public/;

var loaders = [
    {
        test: /\.js$/,
        exclude: exclude,
        loaders: jsxLoader
    },
    {
        test: /\.css$/,
        loader: cssLoader
    },
    {
        test: /\.jpe?g$|\.gif$|\.png$|\.ico|\.svg$|\.woff$|\.ttf$/,
        loader: fileLoader
    },
    {
        test: /\.json$/,
        exclude: /node_modules/,
        loaders: jsonLoader
    },
    {
        test: /\.html$/,
        exclude: exclude,
        loader: htmlLoader
    },
    {
        test: /\.scss$/,
        exclude: exclude,
        loader: sassLoader
    },
    {
        test: /\.ts$/,
        loaders: ['angular2-template-loader', 'ts']
    }
];

module.exports = loaders;