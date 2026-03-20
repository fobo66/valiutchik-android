config.resolve = {
    fallback: {
        fs: false,
        path: false,
        crypto: false,
    }
};

config.devServer = {
  ...config.devServer,
  headers: {
    "Cross-Origin-Embedder-Policy": "require-corp",
    "Cross-Origin-Opener-Policy": "same-origin",
  }
}

const CopyWebpackPlugin = require('copy-webpack-plugin');
config.plugins.push(
    new CopyWebpackPlugin({
        patterns: [
            '../../node_modules/sql.js/dist/sql-wasm.wasm',
            '../../node_modules/sql.js/dist/sql-wasm-browser.wasm'
        ]
    })
);