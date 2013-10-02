#! /usr/bin/env node
var conf = require('fitbit.conf.json');
var async = require('async');
var oa = require('oauth');
var fs = require('fs');
var program = require('commander');

var endpoints = {
    "base_url": "https://api.fitbit.com/1/user/-/",
    "request_token": "https://api.fitbit.com/oauth/request_token",
    "access_token": "https://api.fitbit.com/oauth/access_token",
    "authorize": "https://www.fitbit.com/oauth/authorize"
};

var fitbit = new oa.OAuth(endpoints.request_token,
                          endpoints.access_token,
                          conf.consumer_key,
                          conf.consumer_secret,
                          "1.0",
                          null,
                          "HMAC-SHA1");

var generateSave = function(name, callback) {
    return function(err, data, response) {
        if (err) {
            console.error(">>> Error in saving " + program.prefix + '-' +
                          name + ": " + err);
            callback(null);
        } else {
            var activity = JSON.parse(data);
            fs.writeFile(program.workingDir + '/' + program.prefix + '-' +
                         name + '.json',
                         JSON.stringify(activity[name], null, 2),
                         function(e) {
                             if (e)
                                 console.error(">>> Error in saving " +
                                               program.prefix + '-' +
                                               name + ": " + e);
                             else
                                 console.log(">>> " + program.prefix + '-' +
                                             name + " saved.");
                             callback(null);
                         });
        }
    }
}

var generateCalls = function(calls) {
    var funcs = [];
    calls.forEach(function(resource) {
        var split = resource.split('/');
        var filename = split[0] + '-' + split[1];
        funcs.push(function(callback) {
            fitbit.get(endpoints.base_url + resource, conf.access_token, conf.access_token_secret, generateSave(filename, callback));
        });
    });
    return funcs;
}

var statsCalls = function() {
    async.parallel(generateCalls(["activities/steps/date/today/1m.json", "sleep/startTime/date/today/1m.json"]), process.exit);
}

var authorizeSequence = function() {
    var open = require('open');
    var util = require('util');

    async.waterfall([
        function requestToken(next) {
            fitbit.getOAuthRequestToken(function(e, t, t_s, r) { next(null, e, t, t_s, r); });
        },
        function requestTokenReceived(error, token, token_secret, results, next) {
            if (error)
                console.error('>>> Error in receiving request token: ' + error);
            else {
                console.log('token: ' + token);
                console.log('token_secret: ' + token_secret);
                console.log('results: ' + util.inspect(results));
                console.log('>>> Now sending user to authorize the application...');
                
                open(endpoints.authorize + '?oauth_token=' + token);
                
                program.prompt("Enter your verifier here: ",
                               function inputReceived(verifier) {
                                   next(null, token, token_secret, verifier);
                               });
            }
        },
        function accessToken(token, token_secret, verifier, next) {
            fitbit.getOAuthAccessToken(token, token_secret, verifier, function(e, t, t_s, r) { next(null, e, t, t_s, r); });
        },
        function accessTokenReceived(error, access_token, access_token_secret, results, next) {
            if (error)
                console.error('>>> Error in requesting access token: ' + error);
            else {
                console.log('access_token: ' + access_token);
                console.log('access_token_secret: ' + access_token_secret);
                console.log('results: ' + util.inspect(results));
                conf.access_token = access_token;
                conf.access_token_secret = access_token_secret;
                fs.writeFile('fitbit.conf.json', JSON.stringify(conf, null, 2), function(e) { next(null, e); });
            }
        },
        function checkErrors(err, next) {
            if (err)
                console.error('>>> Error in receiving access tokens: ' + err);
            else
                console.log('>>> Tokens received, written to configuration file.');
            next(null);
        }], statsCalls);
}

program
    .option('-D, --working-dir [path]', 'specify working directory [work]',
            'work')
    .option('-p, --prefix [name]', 'specify filename prefix [fitbit]',
            'fitbit')
    .parse(process.argv);

console.log(">>> Using " + program.workingDir + " as the working directory.");

if (!conf.access_token || !conf.access_token_secret)
    authorizeSequence();
else
    statsCalls();

