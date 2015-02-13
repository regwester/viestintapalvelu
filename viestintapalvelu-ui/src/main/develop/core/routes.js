'use strict';

angular.module('core')
    .config(['$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
            
            //$urlRouterProvider.otherwise('/email');

            var emailUrl = 'views/email/views/';
            var reportUrl = 'views/report/views/';
            var templateUrl = 'views/letter-templates/views/';

            $stateProvider
                .state('email', {
                    url: '/email',
                    templateUrl: emailUrl + 'email.html',
                    controller: 'EmailCtrl'
                })
                    .state('email.savedContent', {
                        url: '',
                        abstract: true,
                        templateUrl: emailUrl + 'savedContent.html',
                        controller: 'TabCtrl'
                    })
                    .state('email.savedContent.drafts', {
                        url: '/drafts',
                        template: '<row-table items="drafts" onselect="selectDraft(id)" limit="10" empty="rowTable.empty.drafts"></row-table>',
                        controller: 'DraftCtrl'
                    })
                    .state('email.savedContent.sentEmails', {
                        url: '/sentEmails',
                        template: '<row-table items="messages" onselect="selectEmail(id)" limit="10" empty="rowTable.empty.messages"></row-table>',
                        controller: 'MessagesCtrl'
                    })
                    .state('email.savedContent.templates', {
                        url: '/templates',
                        templateUrl: emailUrl + 'partials/templates.html'
                    })
                    .state('email.preview', {
                        url: '/preview',
                        templateUrl: emailUrl + 'preview.html',
                        controller: 'PreviewCtrl'
                    })
                .state('email_cancel', {
                    url: '/email/cancel',
                    templateUrl: emailUrl + 'emailCancel.html',
                    controller: 'EmailCancelCtrl'
                })
                .state('report', {
                    url: '/reportMessages/list',
                    templateUrl: reportUrl + 'reportedMessageList.html',
                    controller: 'ReportedMessageListCtrl'
                })
                .state('report_search', {
                    url: '/reportMessages/search',
                    templateUrl: reportUrl + 'reportedMessageList.html',
                    controller: 'ReportedMessageListCtrl'
                })
                .state('report_view', {
                    url: '/reportMessages/view/:messageID',
                    templateUrl: reportUrl + 'reportedMessageView.html',
                    controller: 'ReportedMessageViewCtrl'
                })
                .state('letter-templates', {
                    abstract: true,
                    url: '/letter-templates-ui',
                    templateUrl: templateUrl + 'index.html',
                    controller: 'LetterTemplateIndexCtrl as IndexCtrl'
                })
                .state('letter-templatesinit', {
                    url: '/letter-templates',
                    templateUrl: templateUrl + 'blank.html',
                    controller: 'ValiController'
                })
                .state('letter-templates.overview', {
                        url: '',
                        views: {
                            overview: {
                                templateUrl: templateUrl + 'overview.html',
                                controller: 'LetterTemplateListCtrl'
                            }
                        }
                    })
                    .state('letter-templates.create', {
                        url: '/create',
                        views: {
                            create: {
                                templateUrl: templateUrl + 'create.html',
                                controller: 'LetterTemplateCreateCtrl as CreateCtrl'
                            }
                        }
                    })
                .state('letter-templates_edit', {
                    url: '/letter-templates/edit/:templateId',
                    templateUrl: templateUrl + 'edit.html',
                    controller: 'LetterTemplateEditCtrl'
                })
                .state('letter-templates_view', {
                    url: '/letter-templates/view/:templateId/:state',
                    templateUrl: templateUrl + 'view.html',
                    controller: 'LetterTemplateViewCtrl'
                })
                .state('letter-templates_draft_edit', {
                    url: '/letter-templates/draft_edit/:templatename/:language/:applicationPeriod/?orgoid&fetchTarget',
                    templateUrl: templateUrl + 'editDraft.html',
                    controller: 'EditDraftCtrl'
                })
                .state('letter-templates_draft_create', {
                    url: '/letter-templates/draft_create/:templatename/:language/:applicationPeriod/?orgoid&fetchTarget',
                    templateUrl: templateUrl + 'editDraft.html',
                    controller: 'CreateDraftCtrl'
                })
                .state('templateTreeList', {
                    url: "/templateTreeList",
                    templateUrl: templateUrl + "templateTreeList.html",
                    controller: "TemplateController"
                })
                .state('letter_reports', {
                    url: '/reportLetters',
                    templateUrl: reportUrl + 'reportedLetterList.html',
                    controller: 'ReportedLetterListCtrl'
                })
                .state('letter_batch_view', {
                    url: '/reportLetters/:letterBatchID',
                    templateUrl: reportUrl + 'reportedLetterView.html',
                    controller: 'ReportedLetterViewCtrl'
                });
        }
    ]);
