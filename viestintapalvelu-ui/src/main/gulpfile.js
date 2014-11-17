var gulp = require('gulp');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var sass = require('gulp-sass');
var sourcemaps = require('gulp-sourcemaps');
var del = require('del');
var bower = require('bower');

/*
   init.js files are included first, then other .js files.
   lib folder in assets is excluded from being processed.
*/
var input = {
    scripts: ['webapp/**/init.js', 'webapp/**/*.js', '!webapp/assets/lib/**/*'],
    styles: ['webapp/assets/css/**/*']
};

var output = {
    scripts: 'webapp/js/',
    styles: 'webapp/css/'
};

gulp.task('clean', function(cb){
    del(['build'], cb);
});

gulp.task('scripts', ['clean'], function(){
    return gulp.src(input.scripts)
        .pipe(sourcemaps.init())
            .pipe(uglify())
            .pipe(concat('all.min.js'))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(output.scripts));
});

gulp.task('styles', function() {
    return gulp.src(input.styles)
        .pipe(sourcemaps.init())
            .pipe(sass())
            .pipe(concat('all.css'))
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(output.styles));
});


gulp.task('bower', function(cb) {
    bower.commands.install([], {save: true}, {})
        .on('end', function(installed) {
            cb();
        })
});

gulp.task('watch', function(){
    gulp.watch(input.scripts, ['scripts']);
    gulp.watch(input.styles, ['styles']);
});

gulp.task('build', ['styles', 'scripts']);