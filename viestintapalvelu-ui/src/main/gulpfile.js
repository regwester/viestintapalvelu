var gulp = require('gulp'),
    concat = require('gulp-concat'),
    uglify = require('gulp-uglify'),
    sass = require('gulp-sass'),
    sourcemaps = require('gulp-sourcemaps'),
    bower = require('gulp-bower'),
    del = require('del'),
    processhtml = require('gulp-processhtml');

/*
   The order of init.js files matters, but other files can be added in whatever order.
*/
var input = {
    scripts: [
        'develop/core/init.js',
        'develop/email/init.js',
        'develop/report/init.js',
        'develop/letter-templates/init.js',
        'develop/init.js',
        'develop/**/filters/*.js',
        'develop/**/services/*.js',
        'develop/**/directives/*.js',
        'develop/**/controllers/*.js',
        'develop/**/*.js'],
    styles: ['develop/assets/css/**/*'],
    html: ['develop/**/views/**/*.html']
};

var output = {
    scripts: 'webapp/js/',
    styles: 'webapp/css/',
    html: 'webapp/views/',
    lib: 'webapp/lib/'
};

gulp.task('clean', function(cb){
    del(['webapp/js', 'webapp/css', 'webapp/views', 'webapp/lib'], cb);
});

/* Script prosessing tasks */
var scripts = function() {
    return gulp.src(input.scripts)
        .pipe(sourcemaps.init({loadMaps: true}))
            .pipe(concat('all.min.js'))
            .pipe(uglify({outSourceMap: true}))
        .pipe(sourcemaps.write('./'))
        .pipe(gulp.dest(output.scripts));
};
gulp.task('scripts', ['clean'], scripts);
gulp.task('scripts-watch', scripts);


/* Style processing tasks */
var styles = function() {
    return gulp.src(input.styles)
        //First write inline sourcemaps then strip them to external file (cannot get it working otherwise)
        .pipe(sourcemaps.init())
            .pipe(sass())
            .pipe(concat('all.css'))
            // Catch any SCSS errors and prevent them from crashing gulp
            .on('error', function (error) {
                console.error(error);
                this.emit('end');
            })
        .pipe(sourcemaps.write())
        .pipe(sourcemaps.init({loadMaps: true}))
        .pipe(sourcemaps.write('./'))
        .pipe(gulp.dest(output.styles));
};
gulp.task('styles', ['clean'], styles);
gulp.task('styles-watch', styles);

/* Html processing tasks */
var html = function() {
    return gulp.src(input.html)
        //TODO: processing, now just move files to correct folder
        .pipe(gulp.dest(output.html));
};
gulp.task('html', ['clean'], html);
gulp.task('html-watch', html);

gulp.task('bower', ['scripts', 'styles', 'html'], function() {
    return bower();
        //.pipe(gulp.dest(output.lib)); //Optional: defaults to directory set in ./.bowerrc
});

gulp.task('watch', function(){
    gulp.watch(input.scripts, ['scripts-watch']);
    gulp.watch(input.styles, ['styles-watch']);
    gulp.watch(input.html, ['html-watch']);
});

gulp.task('build', ['styles', 'scripts', 'html', 'bower']);