var gulp = require('gulp');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var sass = require('gulp-sass');
var sourcemaps = require('gulp-sourcemaps');
var del = require('del');

var input = {
    scripts: ['webapp/**/*.js', '!webapp/assets/lib/**/*'],
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

gulp.task('sass', function() {
    return gulp.src(input.styles)
        .pipe(sass())
        .pipe(gulp.dest(output.styles));
});

gulp.task('watch', function(){
    gulp.watch(input.scripts, ['scripts']);
    gulp.watch(input.styles, ['styles']);
});

gulp.task('default', ['watch', 'styles', 'scripts']);
gulp.task('production', ['styles', 'scripts']);