var path = require('path');
module.exports = function (grunt) {
	require('load-grunt-tasks')(grunt);
	require('time-grunt')(grunt);
	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
		bowerrc: grunt.file.readJSON('.bowerrc'),
		paths: {
			frontend: grunt.option('destination') + '/frontend',
			javascripts: grunt.option('destination') + '/frontend/javascripts',
			less: grunt.option('destination') + '/frontend/less',
			images: grunt.option('destination') + '/frontend/images',
			destination: grunt.option('destination') + '/webapp/resources',
			vendors: 'vendors'
		},
		watch: {
			less: {
				files: '<%= paths.less %>/**/*.less',
				tasks: ['less']
			},
			javascript: {
				files: ['<%= paths.javascripts %>/**/*.js', '<%= paths.javascripts %>/**/*.tpl.html', '!<%= paths.javascripts %>/templates.js'],
				tasks: ['javascript']
			}
		},
		less: {
			frontend: {
				options: {
					relativeUrls: true,
					modifyVars: {
						'fonts-path': '"../fonts"',
						'fa-font-path': '"../fonts"'
					}
				},
				files: {
					'<%= paths.destination %>/css/application.css': '<%= paths.less %>/application.less'
				}
			}
		},
		html2js: {
			options: {
				base: grunt.option('destination') + '/frontend/javascripts',
				module: 'workflow.templates',
				singleModule: true,
				useStrict: true,
				htmlmin: {
					collapseBooleanAttributes: true,
					collapseWhitespace: true,
					removeAttributeQuotes: true,
					removeComments: true,
					removeEmptyAttributes: true,
					removeRedundantAttributes: true,
					removeScriptTypeAttributes: true,
					removeStyleLinkTypeAttributes: true
				}
			},
			application: {
				src: ['<%= paths.javascripts %>/**/*.tpl.html'],
				dest: '<%= paths.javascripts %>/templates.js'
			}
		},
		concat: {
			development: {
				files: {
					'<%= paths.destination %>/javascripts/application.js': [
						'<%= paths.vendors %>/angular/angular.js',
						'<%= paths.vendors %>/angular-animate/angular-animate.js',
						'<%= paths.vendors %>/angular-bootstrap/ui-bootstrap.js',
						'<%= paths.vendors %>/angular-bootstrap/ui-bootstrap-tpls.js',
						'<%= paths.vendors %>/angular-resource/angular-resource.js',
						'<%= paths.vendors %>/angular-sanitize/angular-sanitize.js',
						'<%= paths.vendors %>/ui-router/release/angular-ui-router.js',
						'<%= paths.javascripts %>/**/*.js'
					]
				}
			},
			production: {
				files: {
					'<%= paths.frontend %>/application.js': [
						'<%= paths.vendors %>/angular/angular.js',
						'<%= paths.vendors %>/angular-animate/angular-animate.js',
						'<%= paths.vendors %>/angular-bootstrap/ui-bootstrap.js',
						'<%= paths.vendors %>/angular-bootstrap/ui-bootstrap-tpls.js',
						'<%= paths.vendors %>/angular-resource/angular-resource.js',
						'<%= paths.vendors %>/angular-sanitize/angular-sanitize.js',
						'<%= paths.vendors %>/ui-router/release/angular-ui-router.js',
						'<%= paths.javascripts %>/**/*.js'
					]
				}
			}
		},
		uglify: {
			javascript: {
				options: {
					beautify: false
				},
				files: {
					'<%= paths.destination %>/javascripts/application.js': [
						'<%= paths.destination %>/javascripts/application.js'
					]
				}
			}
		},
		copy: {
			frontend: {
				files: [
					{expand: true, flatten: true, src: ['<%= paths.vendors %>/font-awesome/fonts/*'], dest: '<%= paths.destination %>/fonts'},
					{expand: true, flatten: true, src: ['<%= paths.vendors %>/leaflet/dist/images/*'], dest: '<%= paths.destination %>/images'},
					{expand: true, flatten: true, src: ['<%= paths.vendors %>/Leaflet.awesome-markers/dist/images/*'], dest: '<%= paths.destination %>/images'},
					{expand: true, flatten: false, cwd: '<%= paths.vendors %>/source-sans-pro/fonts', src: ['**/*'], dest: '<%= paths.destination %>/fonts'},
					{expand: true, flatten: false, cwd: '<%= paths.images %>/', src: ['**/*'], dest: '<%= paths.destination %>/images'}
				]
			}
		},
		clean: {
			javascripts: ['<%= paths.javascripts %>/templates.js'],
			production: ['<%= paths.frontend %>/application.js']
		}
	});

	grunt.registerTask('javascript', ['html2js', 'concat:development', 'clean']);

	grunt.registerTask('default', ['less', 'javascript', 'copy', 'watch']);
	grunt.registerTask('production', ['less', 'html2js', 'concat:production', 'uglify', 'clean', 'copy']);
};
