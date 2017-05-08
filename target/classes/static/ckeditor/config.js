/**
 * @license Copyright (c) 2003-2017, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	// 需放置在最前面，设置用<br>做换行，而不是<p>标签，避免后台页面列表显示时行高增加
	config.enterMode = CKEDITOR.ENTER_BR; 
    config.shiftEnterMode = CKEDITOR.ENTER_BR;
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
};
