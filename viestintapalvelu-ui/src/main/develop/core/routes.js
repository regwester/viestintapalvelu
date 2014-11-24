'use strict';

angular.module('core')
    .config(['$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/email');

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
                .state('report', { //Change these to substates (use dot notation, change url and configure controllers)
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
                .state('letter-templates_overview', {
                    url: '/letter-templates',
                    templateUrl: templateUrl + 'overview.html',
                    controller: 'LetterTemplateListCtrl'
                })
                .state('letter-templates_create', {
                    url: '/letter-templates/create',
                    templateUrl: templateUrl + 'create.html',
                    controller: 'LetterTemplateCreateCtrl'
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
