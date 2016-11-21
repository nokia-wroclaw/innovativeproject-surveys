var path = require('path');
var util = require('util');
var ExtractTextPlugin = require('extract-text-webpack-plugin');
var webpack = require('webpack');
var webpackFailPlugin = require('webpack-fail-plugin');
var webpackNotifierPlugin = require('webpack-notifier');
var pkg = require('../package.json');

var cssBundle = path.join('css', util.format('[name].%s.css', pkg.version));

var plugins = [
    new webpack.optimize.OccurenceOrderPlugin(),
    new webpackNotifierPlugin(),
    webpackFailPlugin,
    new ExtractTextPlugin(cssBundle, {allChunks: true}),
    new webpack.optimize.DedupePlugin(),
    new webpack.DefinePlugin({'process.env': {
            NODE_ENV: JSON.stringify('production')
        }
    }),
    new webpack.NoErrorsPlugin(),
    new webpack.ProvidePlugin({
        Reflect: 'core-js/es7/reflect'
    })
];

module.exports = plugins;