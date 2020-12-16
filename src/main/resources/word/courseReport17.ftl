<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<?mso-application progid="Word.Document"?>
<pkg:package
        xmlns:pkg="http://schemas.microsoft.com/office/2006/xmlPackage">
    <pkg:part pkg:name="/_rels/.rels" pkg:contentType="application/vnd.openxmlformats-package.relationships+xml" pkg:padding="512">
        <pkg:xmlData>
            <Relationships
                    xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties" Target="docProps/app.xml"/>
                <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" Target="docProps/core.xml"/>
                <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
            </Relationships>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/_rels/document.xml.rels" pkg:contentType="application/vnd.openxmlformats-package.relationships+xml" pkg:padding="256">
        <pkg:xmlData>
            <Relationships
                    xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                <Relationship Id="rId8" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/fontTable" Target="fontTable.xml"/>
                <Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/settings" Target="settings.xml"/>
                <Relationship Id="rId7" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/footer" Target="footer1.xml"/>
                <Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
                <Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/numbering" Target="numbering.xml"/>
                <Relationship Id="rId6" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/endnotes" Target="endnotes.xml"/>
                <Relationship Id="rId5" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/footnotes" Target="footnotes.xml"/>
                <Relationship Id="rId4" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/webSettings" Target="webSettings.xml"/>
                <Relationship Id="rId9" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme" Target="theme/theme1.xml"/>
                [#--图片--]
                [#if data.imageList?? ]
                    [#list data.imageList as imageList]

                        <Relationship Id="${imageList.imageUrl}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="${imageList.imageNameNo}"/>
                    [/#list]
                [/#if]
            </Relationships>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/document.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml">
        <pkg:xmlData>
            <w:document
                    xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
                    xmlns:cx="http://schemas.microsoft.com/office/drawing/2014/chartex"
                    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                    xmlns:o="urn:schemas-microsoft-com:office:office"
                    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                    xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
                    xmlns:v="urn:schemas-microsoft-com:vml"
                    xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
                    xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
                    xmlns:w10="urn:schemas-microsoft-com:office:word"
                    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                    xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                    xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml"
                    xmlns:w16se="http://schemas.microsoft.com/office/word/2015/wordml/symex"
                    xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
                    xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
                    xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
                    xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" mc:Ignorable="w14 w15 w16se wp14">
                <w:body>
                    <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="00C53283">
                        <w:pPr>
                            <w:pStyle w:val="4"/>
                            <w:spacing w:line="288" w:lineRule="auto"/>
                            <w:ind w:left="2153" w:right="2153"/>
                            <w:jc w:val="center"/>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="Times New Roman"/>
                            </w:rPr>
                           [#-- <w:t xml:space="preserve">2017-2018 </w:t>--]
                        </w:r>
                        <w:r>
                            [#if data.educlassCourseInfo?? ]
                                <w:t>${data.educlassCourseInfo.courseTerm}学期《${data.educlassCourseInfo.courseName}》课程反思与持续改进报告（评价部分）</w:t>
                            [/#if]
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="00C53283" w:rsidRDefault="005D2C56" w:rsidP="00C53283">
                        <w:pPr>
                            <w:spacing w:before="101"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:sz w:val="20"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="20"/>
                            </w:rPr>
                            <w:t>课程责任人:</w:t>
                        </w:r>

                        <w:r w:rsidR="00C53283">
                            <w:rPr>
                                <w:sz w:val="20"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:sz w:val="20"/>
                            </w:rPr>
                            <w:t>[#if data.educlassCourseInfo.teamLeader??]${data.educlassCourseInfo.teamLeader}[/#if]</w:t>
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="005D2C56">
                        <w:pPr>
                            <w:pStyle w:val="a3"/>
                            <w:spacing w:before="7" w:line="360" w:lineRule="auto"/>
                            <w:rPr>
                                <w:sz w:val="27"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w:rsidR="00C53283" w:rsidRPr="005D2C56" w:rsidRDefault="00C53283" w:rsidP="005D2C56">
                        <w:pPr>
                            <w:spacing w:before="3" w:line="360" w:lineRule="auto"/>
                            <w:ind w:left="478"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="005D2C56">
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>一</w:t>
                        </w:r>
                        <w:r w:rsidR="005D2C56" w:rsidRPr="005D2C56">
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsiaTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>、</w:t>
                        </w:r>
                        <w:r w:rsidRPr="005D2C56">
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>课程简介</w:t>
                        </w:r>
                    </w:p>

                    [#if data.achieveReportInfo.courseInfo??]
                        [#assign mainContentMap = data.achieveReportInfo.courseInfo]
                        [#if mainContentMap?is_hash]
                            [#if mainContentMap.isNonChinese == 0]
                                <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                    <w:pPr>
                                        <w:spacing w:line="480" w:lineRule="exact"/>
                                        <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                        </w:rPr>
                                    </w:pPr>

                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                        </w:rPr>
                                        <w:t>
                                            [#--[#if data.achieveReportInfo.targetRequire??]
                                                ${data.achieveReportInfo.targetRequire}
                                            [/#if]--]
                                        </w:t>
                                    </w:r>
                                </w:p>
                            [#else]
                                [#assign paragraphDataList = mainContentMap.paragraphDataList]
                                [#if paragraphDataList?? && paragraphDataList?size >0]
                                    [#list paragraphDataList as paragraph]
                                        <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                            <w:pPr>
                                                <w:spacing w:line="480" w:lineRule="exact"/>
                                                <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                                </w:rPr>
                                            </w:pPr>
                                            [#if paragraph?? && paragraph?size > 0]
                                                [#list paragraph as sentence]
                                                    [#if sentence?? && sentence.isNonChinese??]
                                                        [#if sentence.isNonChinese == 1]
                                                            <w:r>
                                                                <w:rPr>
                                                                    <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                                                </w:rPr>
                                                                <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                            </w:r>
                                                        [#else ]
                                                            <w:r>
                                                                <w:rPr>
                                                                    <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                                                </w:rPr>
                                                                <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                            </w:r>
                                                        [/#if]
                                                    [/#if]
                                                [/#list]
                                            [/#if]
                                        </w:p>
                                    [/#list]
                                [/#if]
                            [/#if]
                        [/#if]
                    [/#if]




                    <w:p w:rsidR="00C53283" w:rsidRPr="005D2C56" w:rsidRDefault="005D2C56" w:rsidP="005D2C56">
                        <w:pPr>
                            <w:spacing w:before="172" w:line="360" w:lineRule="auto"/>
                            <w:ind w:left="478"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>二</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsiaTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>、</w:t>
                        </w:r>
                        <w:r w:rsidR="00C53283" w:rsidRPr="005D2C56">
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>课程学习目标及能力培养</w:t>
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="009128C0" w:rsidRPr="005D2C56" w:rsidRDefault="009128C0" w:rsidP="005D2C56">
                        <w:pPr>
                            <w:spacing w:before="1" w:line="360" w:lineRule="auto"/>
                            <w:ind w:right="469" w:firstLineChars="400" w:firstLine="960"/>
                            <w:rPr>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="005D2C56">
                            <w:rPr>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>通过本课程的学习，要求学生达到以下目标。</w:t>
                        </w:r>
                    </w:p>
                    [#if data.courseAllIndication ??  && data.courseAllIndication?size>0]
                        [#list  data.courseAllIndication as indicationList]
                            <w:p w:rsidR="009128C0" w:rsidRPr="005D2C56" w:rsidRDefault="009128C0" w:rsidP="005D2C56">
                                <w:pPr>
                                    <w:spacing w:before="1" w:line="360" w:lineRule="auto"/>
                                    <w:ind w:right="469" w:firstLineChars="400" w:firstLine="960"/>
                                    <w:rPr>
                                        <w:sz w:val="24"/>
                                        <w:szCs w:val="24"/>
                                    </w:rPr>
                                </w:pPr>
                                <w:r w:rsidRPr="005D2C56">
                                    <w:rPr>
                                        <w:sz w:val="24"/>
                                        <w:szCs w:val="24"/>
                                    </w:rPr>
                                    <w:t>课程目标${indicationList.sort}:${indicationList.content}</w:t>
                                </w:r>
                            </w:p>
                        [/#list]
                    [/#if]
                    <w:p w:rsidR="009128C0" w:rsidRDefault="00C53283" w:rsidP="00660E30">
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="left" w:pos="1058"/>
                            </w:tabs>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:ind w:firstLineChars="200" w:firstLine="468"/>
                            <w:rPr>
                                <w:spacing w:val="-3"/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="005D2C56">
                            <w:rPr>
                                <w:spacing w:val="-3"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>课程学习目标与专业毕业要求指标点的关系矩阵</w:t>
                        </w:r>
                        <w:r w:rsidR="005D2C56">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:spacing w:val="-3"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>如下</w:t>
                        </w:r>
                        <w:r w:rsidR="005D2C56">
                            <w:rPr>
                                <w:spacing w:val="-3"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>：</w:t>
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="009128C0" w:rsidRPr="0053123F" w:rsidRDefault="009128C0" w:rsidP="009128C0">
                        <w:pPr>
                            <w:spacing w:beforeLines="50" w:before="156" w:line="276" w:lineRule="auto"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="黑体" w:eastAsia="黑体" w:hAnsi="黑体"/>
                                <w:b/>
                                <w:color w:val="000000"/>
                                <w:sz w:val="21"/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0053123F">
                            <w:rPr>
                                <w:rFonts w:ascii="黑体" w:eastAsia="黑体" w:hAnsi="黑体"/>
                                <w:b/>
                                <w:color w:val="000000"/>
                                <w:sz w:val="21"/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>表1</w:t>
                        </w:r>
                        <w:r w:rsidR="0053123F" w:rsidRPr="0053123F">
                            <w:rPr>
                                <w:rFonts w:ascii="黑体" w:eastAsia="黑体" w:hAnsi="黑体"/>
                                <w:b/>
                                <w:color w:val="000000"/>
                                <w:sz w:val="21"/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidRPr="0053123F">
                            <w:rPr>
                                <w:rFonts w:ascii="黑体" w:eastAsia="黑体" w:hAnsi="黑体"/>
                                <w:b/>
                                <w:color w:val="000000"/>
                                <w:sz w:val="21"/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>课程目标与毕业要求关系矩阵</w:t>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblW w:w="7989" w:type="dxa"/>
                            <w:jc w:val="center"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:insideH w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:insideV w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            </w:tblBorders>
                            <w:tblLayout w:type="fixed"/>
                            <w:tblLook w:val="04A0" w:firstRow="1" w:lastRow="0" w:firstColumn="1" w:lastColumn="0" w:noHBand="0" w:noVBand="1"/>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="690"/>
                            <w:gridCol w:w="1436"/>
                            <w:gridCol w:w="5863"/>
                        </w:tblGrid>
                        <w:tr w:rsidR="009128C0" w:rsidTr="009128C0">
                            <w:trPr>
                                <w:trHeight w:val="290"/>
                                <w:jc w:val="center"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="690" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="D8D8D8" w:themeFill="background1" w:themeFillShade="D8"/>
                                </w:tcPr>
                                <w:p w:rsidR="009128C0" w:rsidRPr="005D2C56" w:rsidRDefault="009128C0" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:spacing w:line="276" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:b/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="005D2C56">
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:b/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>序号</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1436" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="D8D8D8" w:themeFill="background1" w:themeFillShade="D8"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="009128C0" w:rsidRPr="005D2C56" w:rsidRDefault="009128C0" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:spacing w:line="276" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:b/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="005D2C56">
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:b/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>课程目标</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="5863" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="D8D8D8" w:themeFill="background1" w:themeFillShade="D8"/>
                                </w:tcPr>
                                <w:p w:rsidR="009128C0" w:rsidRPr="005D2C56" w:rsidRDefault="009128C0" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:spacing w:line="276" w:lineRule="auto"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:b/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="005D2C56">
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:b/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>毕业要求指标点</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [#if data.indicationPointList ??  && data.indicationPointList?size>0]
                            [#list  data.indicationPointList as indicationPointList]
                                <w:tr w:rsidR="009128C0" w:rsidTr="009128C0">
                                    <w:trPr>
                                        <w:trHeight w:val="50"/>
                                        <w:jc w:val="center"/>
                                    </w:trPr>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="690" w:type="dxa"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="009128C0" w:rsidRPr="005D2C56" w:rsidRDefault="009128C0" w:rsidP="009128C0">
                                            <w:pPr>
                                                <w:spacing w:line="276" w:lineRule="auto"/>
                                                <w:jc w:val="center"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                                    <w:sz w:val="21"/>
                                                    <w:szCs w:val="21"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="005D2C56">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                                    <w:sz w:val="21"/>
                                                    <w:szCs w:val="21"/>
                                                </w:rPr>
                                                <w:t>${indicationPointList.num}</w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="1436" w:type="dxa"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                [#list  data.indicationList as indicationList]
                                    [#if indicationPointList.pointId == indicationList.pointId]
                                        <w:p w:rsidR="009128C0" w:rsidRPr="005D2C56" w:rsidRDefault="009128C0" w:rsidP="009128C0">
                                            <w:pPr>
                                                <w:spacing w:line="276" w:lineRule="auto"/>
                                                <w:jc w:val="center"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                                    <w:sz w:val="21"/>
                                                    <w:szCs w:val="21"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="005D2C56">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                                    <w:sz w:val="21"/>
                                                    <w:szCs w:val="21"/>
                                                </w:rPr>
                                                <w:t>课程${indicationList.indicationName}</w:t>
                                            </w:r>
                                        </w:p>
                                    [/#if]
                                [/#list]
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="5863" w:type="dxa"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="009128C0" w:rsidRPr="005D2C56" w:rsidRDefault="009128C0" w:rsidP="005D2C56">
                                            <w:pPr>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                                    <w:sz w:val="21"/>
                                                    <w:szCs w:val="21"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="005D2C56">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                                    <w:sz w:val="21"/>
                                                    <w:szCs w:val="21"/>
                                                    <w:lang w:val="en-US"/>
                                                </w:rPr>
                                                <w:t>${indicationPointList.graduateIndexNum}.${indicationPointList.index_num} ${indicationPointList.pointCont}</w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                </w:tr>

                            [/#list]
                        [/#if]
                    </w:tbl>

                    <w:p w:rsidR="009128C0" w:rsidRDefault="009128C0" w:rsidP="009128C0">
                        <w:pPr>
                            <w:pStyle w:val="a5"/>
                            <w:tabs>
                                <w:tab w:val="left" w:pos="1058"/>
                            </w:tabs>
                            <w:ind w:left="1057" w:firstLine="0"/>
                            <w:rPr>
                                <w:sz w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w:rsidR="00C53283" w:rsidRPr="0053123F" w:rsidRDefault="00C53283" w:rsidP="0053123F">
                        <w:pPr>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:ind w:left="478"/>
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0053123F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>三</w:t>
                        </w:r>
                        <w:r w:rsidR="0053123F" w:rsidRPr="0053123F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>、</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0053123F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>教学方法</w:t>
                        </w:r>
                    </w:p>
                    [#if data.achieveReportInfo.teacherMothed??]
                    [#--富文本--]
                        [#if data.achieveReportInfo.teacherMothed?is_string && data.achieveReportInfo.teacherMothed?index_of("<w:") != -1 && data.achieveReportInfo.teacherMothed?index_of("</w:") != -1]
                            ${data.achieveReportInfo.teacherMothed}
                        [#else ]
                        [#--下面就是是否有回车 有换行 自成一段--]
                            [#assign mainContentMap = data.achieveReportInfo.teacherMothed]
                            [#if mainContentMap?is_hash]
                                [#if mainContentMap.isNonChinese == 0]
                                    <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                        <w:pPr>
                                            <w:spacing w:line="480" w:lineRule="exact"/>
                                            <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                            <w:rPr>
                                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                <w:spacing w:val="-4"/>
                                                <w:sz w:val="24"/>
                                                <w:szCs w:val="24"/>
                                            </w:rPr>
                                        </w:pPr>

                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                            </w:rPr>
                                            <w:t>
                                                [#--[#if data.achieveReportInfo.targetRequire??]
                                                    ${data.achieveReportInfo.targetRequire}
                                                [/#if]--]
                                            </w:t>
                                        </w:r>
                                    </w:p>
                                [#else]
                                    [#assign paragraphDataList = mainContentMap.paragraphDataList]
                                    [#if paragraphDataList?? && paragraphDataList?size >0]
                                        [#list paragraphDataList as paragraph]
                                            <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                                <w:pPr>
                                                    <w:spacing w:line="480" w:lineRule="exact"/>
                                                    <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                    <w:rPr>
                                                        <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                        <w:spacing w:val="-4"/>
                                                        <w:sz w:val="24"/>
                                                        <w:szCs w:val="24"/>
                                                    </w:rPr>
                                                </w:pPr>
                                                [#if paragraph?? && paragraph?size > 0]
                                                    [#list paragraph as sentence]
                                                        [#if sentence?? && sentence.isNonChinese??]
                                                            [#if sentence.isNonChinese == 1]
                                                                <w:r>
                                                                    <w:rPr>
                                                                        <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                                        <w:spacing w:val="-4"/>
                                                                        <w:sz w:val="24"/>
                                                                        <w:szCs w:val="24"/>
                                                                    </w:rPr>
                                                                    <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                </w:r>
                                                            [#else ]
                                                                <w:r>
                                                                    <w:rPr>
                                                                        <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                                        <w:spacing w:val="-4"/>
                                                                        <w:sz w:val="24"/>
                                                                        <w:szCs w:val="24"/>
                                                                    </w:rPr>
                                                                    <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                </w:r>
                                                            [/#if]
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                            </w:p>
                                        [/#list]
                                    [/#if]
                                [/#if]
                            [/#if]
                        [/#if]
                    [/#if]
                    <w:p w:rsidR="00C53283" w:rsidRPr="0053123F" w:rsidRDefault="00C53283" w:rsidP="0053123F">
                        <w:pPr>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:ind w:left="478"/>
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0053123F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>四．评估方法</w:t>
                        </w:r>
                    </w:p>
                    [#if data.achieveReportInfo.assessMothed??]
                    [#--富文本--]
                        [#if data.achieveReportInfo.assessMothed?is_string && data.achieveReportInfo.assessMothed?index_of("<w:") != -1 && data.achieveReportInfo.assessMothed?index_of("</w:") != -1]
                            ${data.achieveReportInfo.assessMothed}
                        [#else ]
                        [#--下面就是是否有回车 有换行 自成一段--]
                            [#assign mainContentMap = data.achieveReportInfo.assessMothed]
                            [#if mainContentMap?is_hash]
                                [#if mainContentMap.isNonChinese == 0]
                                    <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                        <w:pPr>
                                            <w:spacing w:line="480" w:lineRule="exact"/>
                                            <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                            <w:rPr>
                                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                <w:spacing w:val="-4"/>
                                                <w:sz w:val="24"/>
                                                <w:szCs w:val="24"/>
                                            </w:rPr>
                                        </w:pPr>

                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                <w:spacing w:val="-4"/>
                                                <w:sz w:val="24"/>
                                                <w:szCs w:val="24"/>
                                            </w:rPr>
                                            <w:t>
                                                [#--[#if data.achieveReportInfo.targetRequire??]
                                                    ${data.achieveReportInfo.targetRequire}
                                                [/#if]--]
                                            </w:t>
                                        </w:r>
                                    </w:p>
                                [#else]
                                    [#assign paragraphDataList = mainContentMap.paragraphDataList]
                                    [#if paragraphDataList?? && paragraphDataList?size >0]
                                        [#list paragraphDataList as paragraph]
                                            <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                                <w:pPr>
                                                    <w:spacing w:line="480" w:lineRule="exact"/>
                                                    <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                    <w:rPr>
                                                        <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                        <w:spacing w:val="-4"/>
                                                        <w:sz w:val="24"/>
                                                        <w:szCs w:val="24"/>
                                                    </w:rPr>
                                                </w:pPr>
                                                [#if paragraph?? && paragraph?size > 0]
                                                    [#list paragraph as sentence]
                                                        [#if sentence?? && sentence.isNonChinese??]
                                                            [#if sentence.isNonChinese == 1]
                                                                <w:r>
                                                                    <w:rPr>
                                                                        <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                                        <w:spacing w:val="-4"/>
                                                                        <w:sz w:val="24"/>
                                                                        <w:szCs w:val="24"/>
                                                                    </w:rPr>
                                                                    <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                </w:r>
                                                            [#else ]
                                                                <w:r>
                                                                    <w:rPr>
                                                                        <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                                        <w:spacing w:val="-4"/>
                                                                        <w:sz w:val="24"/>
                                                                        <w:szCs w:val="24"/>
                                                                    </w:rPr>
                                                                    <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                </w:r>
                                                            [/#if]
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                            </w:p>
                                        [/#list]
                                    [/#if]
                                [/#if]
                            [/#if]
                        [/#if]
                    [/#if]
                    <w:p w:rsidR="00C53283" w:rsidRPr="0053123F" w:rsidRDefault="00C53283" w:rsidP="0053123F">
                        <w:pPr>
                            <w:pStyle w:val="a5"/>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="2"/>
                            </w:numPr>
                            <w:tabs>
                                <w:tab w:val="left" w:pos="740"/>
                            </w:tabs>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0053123F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:spacing w:val="-1"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>细化可测评的课程学习目标</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0053123F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>（</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0053123F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:spacing w:val="-1"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>预期学习成果</w:t>
                        </w:r>
                        <w:r w:rsidRPr="0053123F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>）</w:t>
                        </w:r>
                    </w:p>
                    [#if data.achieveReportInfo.courseLearTarget??]
                    [#--富文本--]
                        [#if data.achieveReportInfo.courseLearTarget?is_string && data.achieveReportInfo.courseLearTarget?index_of("<w:") != -1 && data.achieveReportInfo.courseLearTarget?index_of("</w:") != -1]
                            ${data.achieveReportInfo.courseLearTarget}
                        [#else ]
                        [#--下面就是是否有回车 有换行 自成一段--]
                            [#assign mainContentMap = data.achieveReportInfo.courseLearTarget]
                            [#if mainContentMap?is_hash]
                                [#if mainContentMap.isNonChinese == 0]
                                    <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                        <w:pPr>
                                            <w:spacing w:line="480" w:lineRule="exact"/>
                                            <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                            <w:rPr>
                                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                <w:spacing w:val="-4"/>
                                                <w:sz w:val="24"/>
                                                <w:szCs w:val="24"/>
                                            </w:rPr>
                                        </w:pPr>

                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                <w:spacing w:val="-4"/>
                                                <w:sz w:val="24"/>
                                                <w:szCs w:val="24"/>
                                            </w:rPr>
                                            <w:t>
                                                [#--[#if data.achieveReportInfo.targetRequire??]
                                                    ${data.achieveReportInfo.targetRequire}
                                                [/#if]--]
                                            </w:t>
                                        </w:r>
                                    </w:p>
                                [#else]
                                    [#assign paragraphDataList = mainContentMap.paragraphDataList]
                                    [#if paragraphDataList?? && paragraphDataList?size >0]
                                        [#list paragraphDataList as paragraph]
                                            <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                                <w:pPr>
                                                    <w:spacing w:line="480" w:lineRule="exact"/>
                                                    <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                    <w:rPr>
                                                        <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                        <w:spacing w:val="-4"/>
                                                        <w:sz w:val="24"/>
                                                        <w:szCs w:val="24"/>
                                                    </w:rPr>
                                                </w:pPr>
                                                [#if paragraph?? && paragraph?size > 0]
                                                    [#list paragraph as sentence]
                                                        [#if sentence?? && sentence.isNonChinese??]
                                                            [#if sentence.isNonChinese == 1]
                                                                <w:r>
                                                                    <w:rPr>
                                                                        <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                                        <w:spacing w:val="-4"/>
                                                                        <w:sz w:val="24"/>
                                                                        <w:szCs w:val="24"/>
                                                                    </w:rPr>
                                                                    <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                </w:r>
                                                            [#else ]
                                                                <w:r>
                                                                    <w:rPr>
                                                                        <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                                                        <w:spacing w:val="-4"/>
                                                                        <w:sz w:val="24"/>
                                                                        <w:szCs w:val="24"/>
                                                                    </w:rPr>
                                                                    <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                </w:r>
                                                            [/#if]
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                            </w:p>
                                        [/#list]
                                    [/#if]
                                [/#if]
                            [/#if]
                        [/#if]
                    [/#if]
                    <w:p w:rsidR="00C53283" w:rsidRPr="00687F7F" w:rsidRDefault="00C53283" w:rsidP="00C53283">
                        <w:pPr>
                            <w:pStyle w:val="a5"/>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="2"/>
                            </w:numPr>
                            <w:tabs>
                                <w:tab w:val="left" w:pos="740"/>
                            </w:tabs>
                            <w:spacing w:before="3" w:line="360" w:lineRule="auto"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei"/>
                                <w:b/>
                                <w:sz w:val="16"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="00687F7F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>试卷设计（</w:t>
                        </w:r>
                        <w:r w:rsidRPr="00687F7F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:spacing w:val="-1"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>试卷蓝图，</w:t>
                        </w:r>
                        <w:r w:rsidRPr="00687F7F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"/>
                                <w:b/>
                                <w:spacing w:val="-5"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>Test</w:t>
                        </w:r>
                        <w:r w:rsidRPr="00687F7F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"/>
                                <w:b/>
                                <w:spacing w:val="-1"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidRPr="00687F7F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>blueprint</w:t>
                        </w:r>
                        <w:r w:rsidRPr="00687F7F">
                            <w:rPr>
                                <w:rFonts w:asciiTheme="minorEastAsia" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>）</w:t>
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="00C53283">
                        <w:pPr>
                            <w:spacing w:before="1"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve">表2 </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="Times New Roman"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>${data.educlassCourseInfo.courseTerm}</w:t>
                        </w:r>
                        <w:r w:rsidR="00B2075B">
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsiaTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>${data.educlassCourseInfo.courseName}</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>期末试卷设计</w:t>
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="00C53283">
                        <w:pPr>
                            <w:pStyle w:val="a3"/>
                            <w:spacing w:before="17" w:after="1"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei"/>
                                <w:b/>
                                <w:sz w:val="12"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblStyle w:val="TableNormal"/>
                            <w:tblW w:w="9214" w:type="dxa"/>
                            <w:tblInd w:w="-5" w:type="dxa"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                <w:left w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                <w:insideH w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                <w:insideV w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                            </w:tblBorders>
                            <w:tblLayout w:type="fixed"/>
                            <w:tblLook w:val="01E0" w:firstRow="1" w:lastRow="1" w:firstColumn="1" w:lastColumn="1" w:noHBand="0" w:noVBand="0"/>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="1323"/>
                            <w:gridCol w:w="2788"/>
                            <w:gridCol w:w="709"/>
                            <w:gridCol w:w="709"/>
                            <w:gridCol w:w="708"/>
                            <w:gridCol w:w="709"/>
                            <w:gridCol w:w="709"/>
                            <w:gridCol w:w="850"/>
                            <w:gridCol w:w="709"/>
                        </w:tblGrid>
                        <w:tr w:rsidR="00C53283" w:rsidTr="003A62D2">
                            <w:trPr>
                                <w:trHeight w:val="530"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1323" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="77"/>
                                        <w:ind w:left="222"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:lastRenderedPageBreak/>
                                        <w:t>课程</w:t>
                                    </w:r>
                                </w:p>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="162"/>
                                        <w:ind w:left="222"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>目标</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2788" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="9"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="1"/>
                                        <w:ind w:left="720"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>考核内容</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3544" w:type="dxa"/>
                                    <w:gridSpan w:val="5"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="69"/>
                                        <w:ind w:left="940"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>行为</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="Times New Roman"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>/</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>技能（题数）</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="850" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="9"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="1"/>
                                        <w:ind w:left="226"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>分数</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="709" w:type="dxa"/>
                                    <w:vMerge w:val="restart"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="77"/>
                                        <w:ind w:right="210"/>
                                        <w:jc w:val="right"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>占比</w:t>
                                    </w:r>
                                </w:p>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="162"/>
                                        <w:ind w:right="114"/>
                                        <w:jc w:val="right"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:w w:val="95"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>（</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="Times New Roman"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:w w:val="95"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>%</w:t>
                                    </w:r>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:w w:val="95"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>）</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr w:rsidR="00C53283" w:rsidTr="00E11A30">
                            <w:trPr>
                                <w:trHeight w:val="532"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1323" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:tcBorders>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:rPr>
                                            <w:sz w:val="2"/>
                                            <w:szCs w:val="2"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2788" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:tcBorders>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:rPr>
                                            <w:sz w:val="2"/>
                                            <w:szCs w:val="2"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="709" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="72"/>
                                        <w:ind w:left="124" w:right="112"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>记忆</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="709" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="72"/>
                                        <w:ind w:left="135" w:right="125"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>理解</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="708" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="72"/>
                                        <w:ind w:left="121" w:right="113"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>应用</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="709" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="72"/>
                                        <w:ind w:left="154"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>评价</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="709" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:spacing w:before="72"/>
                                        <w:ind w:left="124" w:right="110"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:color w:val="FFFFFF"/>
                                            <w:sz w:val="20"/>
                                        </w:rPr>
                                        <w:t>创造</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="850" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:tcBorders>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:rPr>
                                            <w:sz w:val="2"/>
                                            <w:szCs w:val="2"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="709" w:type="dxa"/>
                                    <w:vMerge/>
                                    <w:tcBorders>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="6F2F9F"/>
                                </w:tcPr>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="009128C0">
                                    <w:pPr>
                                        <w:rPr>
                                            <w:sz w:val="2"/>
                                            <w:szCs w:val="2"/>
                                        </w:rPr>
                                    </w:pPr>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [#list data.endTermIndicationList as indicationList]
                        [#if indicationList.indicationMaxScore !=0]

                        <w:tr w:rsidR="00E11A30" w:rsidTr="000C2474">
                            <w:trPr>
                                <w:trHeight w:val="532"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1323" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                </w:tcPr>
                                <w:p w:rsidR="00E11A30" w:rsidRDefault="00E11A30" w:rsidP="00E11A30">
                                    <w:pPr>
                                        <w:rPr>
                                            <w:sz w:val="2"/>
                                            <w:szCs w:val="2"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="009D51B8">
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>目标${indicationList.sort}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2788" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                </w:tcPr>
                                <w:p w:rsidR="00E11A30" w:rsidRDefault="00E11A30" w:rsidP="00E11A30">
                                    <w:pPr>
                                        <w:rPr>
                                            <w:sz w:val="2"/>
                                            <w:szCs w:val="2"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003A62D2">
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                            <w:lang w:eastAsia="zh-CN"/>
                                        </w:rPr>
                                        <w:t>[#if indicationList.checkContent??]${indicationList.checkContent}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="709" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>

                                <w:p w:rsidR="00E11A30" w:rsidRPr="009D51B8" w:rsidRDefault="00E11A30" w:rsidP="00EC7E65">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:ind w:left="6"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="009D51B8">
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:w w:val="99"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        [#--记忆题数--]
                                        <w:t>[#if indicationList.memoryNum??]${indicationList.memoryNum}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="709" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>

                                <w:p w:rsidR="00E11A30" w:rsidRPr="009D51B8" w:rsidRDefault="00E11A30" w:rsidP="00EC7E65">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:ind w:left="6"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="009D51B8">
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:w w:val="99"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        [#--理解题数--]
                                        <w:t>[#if indicationList.understandNum??]${indicationList.understandNum}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="709" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>

                                <w:p w:rsidR="00E11A30" w:rsidRPr="009D51B8" w:rsidRDefault="00E11A30" w:rsidP="00EC7E65">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:ind w:left="6"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="009D51B8">
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:w w:val="99"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        [#--应用题数--]
                                        <w:t>[#if indicationList.applyNum??]${indicationList.applyNum}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="709" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>

                                <w:p w:rsidR="00E11A30" w:rsidRPr="009D51B8" w:rsidRDefault="00E11A30" w:rsidP="00EC7E65">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:ind w:left="6"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="009D51B8">
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:w w:val="99"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        [#--评价题数--]
                                        <w:t>[#if indicationList.assessNum??]${indicationList.assessNum}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="709" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>

                                <w:p w:rsidR="00E11A30" w:rsidRPr="009D51B8" w:rsidRDefault="00E11A30" w:rsidP="00EC7E65">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:ind w:left="6"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="009D51B8">
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:w w:val="99"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        [#--创造题数--]
                                        <w:t>[#if indicationList.createNum??]${indicationList.createNum}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="850" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>

                                <w:p w:rsidR="00E11A30" w:rsidRPr="009D51B8" w:rsidRDefault="00E11A30" w:rsidP="00EC7E65">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:ind w:left="308" w:right="294"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="009D51B8">
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${indicationList.indicationMaxScore}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="709" w:type="dxa"/>
                                    <w:shd w:val="clear" w:color="auto" w:fill="FFFFFF" w:themeFill="background1"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>

                                <w:p w:rsidR="00E11A30" w:rsidRPr="009D51B8" w:rsidRDefault="00E11A30" w:rsidP="00EC7E65">
                                    <w:pPr>
                                        <w:pStyle w:val="TableParagraph"/>
                                        <w:ind w:left="132" w:right="120"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="009D51B8">
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            <w:sz w:val="21"/>
                                            <w:szCs w:val="21"/>
                                        </w:rPr>
                                        <w:t>${indicationList.indicatMakeUp}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>

                        [/#if]
                        [/#list]
                    </w:tbl>
                    <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="00C53283">
                        <w:pPr>
                            <w:pStyle w:val="a3"/>
                            <w:spacing w:before="11"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei"/>
                                <w:b/>
                                <w:sz w:val="3"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="00333C69">
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="left" w:pos="838"/>
                            </w:tabs>
                            <w:spacing w:line="472" w:lineRule="auto"/>
                            <w:ind w:right="468" w:firstLineChars="250" w:firstLine="505"/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:spacing w:val="-4"/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="00333C69">
                            <w:rPr>
                                <w:spacing w:val="-4"/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            [#--<w:t>本试卷根据课程目标设计了总共 30 个问题</w:t>--]
                            <w:t>对于本课程的试卷设计，如表2所示。试卷中 ${data.indicationMarkUpString}。[#if data.achieveReportInfo.endPaper??]${data.achieveReportInfo.endPaper}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidR="00741DEA">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:spacing w:val="-4"/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>。</w:t>
                        </w:r>
                        <w:r w:rsidRPr="00333C69">
                            <w:rPr>
                                <w:spacing w:val="-4"/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>通过本试卷考核，能够在一定程度上说明课程目标达成情况。</w:t>
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="00C53283" w:rsidRPr="00874A11" w:rsidRDefault="00C53283" w:rsidP="00162820">
                        <w:pPr>
                            <w:pStyle w:val="a5"/>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="2"/>
                            </w:numPr>
                            <w:tabs>
                                <w:tab w:val="left" w:pos="740"/>
                            </w:tabs>
                            <w:spacing w:before="89"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>课程学习成果评估表</w:t>
                        </w:r>
                    </w:p>
                    [#assign tableNum=3]
                    [#--循环课程目标得各个目标的评估表--]
                    [#if data.courseAllIndication ??  && data.courseAllIndication?size>0]
                    [#list  data.courseAllIndication as indicationList]

                        <w:p w:rsidR="00874A11" w:rsidRPr="00874A11" w:rsidRDefault="00874A11" w:rsidP="00874A11">
                            <w:pPr>
                                <w:pStyle w:val="a5"/>
                                <w:spacing w:before="144"/>
                                <w:ind w:left="739" w:right="105" w:firstLine="0"/>
                                <w:jc w:val="center"/>
                                <w:rPr>
                                    <w:rFonts w:ascii="黑体" w:eastAsia="黑体" w:hAnsi="黑体"/>
                                    <w:b/>
                                    <w:spacing w:val="-1"/>
                                    <w:sz w:val="21"/>
                                </w:rPr>
                            </w:pPr>
                            <w:r w:rsidRPr="00874A11">
                                <w:rPr>
                                    <w:rFonts w:ascii="黑体" w:eastAsia="黑体" w:hAnsi="黑体" w:hint="eastAsia"/>
                                    <w:b/>
                                    <w:sz w:val="21"/>
                                </w:rPr>
                                <w:t>表${tableNum}</w:t>
                            </w:r>
                            [#assign tableNum += 1]
                            <w:r w:rsidRPr="00874A11">
                                <w:rPr>
                                    <w:rFonts w:ascii="黑体" w:eastAsia="黑体" w:hAnsi="黑体"/>
                                    <w:b/>
                                    <w:spacing w:val="51"/>
                                    <w:sz w:val="21"/>
                                </w:rPr>
                                <w:t xml:space="preserve"></w:t>
                            </w:r>
                            <w:r w:rsidRPr="00874A11">
                                <w:rPr>
                                    <w:rFonts w:ascii="黑体" w:eastAsia="黑体" w:hAnsi="黑体" w:hint="eastAsia"/>
                                    <w:b/>
                                    <w:sz w:val="21"/>
                                </w:rPr>
                                <w:t>课程学习目标${indicationList.sort}的评估结果</w:t>
                            </w:r>

                        </w:p>
                        <w:p w:rsidR="00874A11" w:rsidRPr="00874A11" w:rsidRDefault="00874A11" w:rsidP="00874A11">
                            <w:pPr>
                                <w:pStyle w:val="a5"/>
                                <w:tabs>
                                    <w:tab w:val="left" w:pos="740"/>
                                </w:tabs>
                                <w:spacing w:before="89"/>
                                <w:ind w:left="739" w:firstLine="0"/>
                                <w:jc w:val="right"/>
                                <w:rPr>
                                    <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                    <w:b/>
                                    <w:sz w:val="21"/>
                                </w:rPr>
                            </w:pPr>
                        </w:p>
                        <w:tbl>
                            <w:tblPr>
                                <w:tblW w:w="9497" w:type="dxa"/>
                                <w:tblLook w:val="04A0" w:firstRow="1" w:lastRow="0" w:firstColumn="1" w:lastColumn="0" w:noHBand="0" w:noVBand="1"/>
                            </w:tblPr>
                            <w:tblGrid>
                                <w:gridCol w:w="1843"/>
                                <w:gridCol w:w="2126"/>
                                <w:gridCol w:w="1418"/>
                                <w:gridCol w:w="2409"/>
                                <w:gridCol w:w="1701"/>
                            </w:tblGrid>
                            <w:tr w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidTr="001E78CB">
                                <w:trPr>
                                    <w:trHeight w:val="675"/>
                                </w:trPr>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="3969" w:type="dxa"/>
                                        <w:gridSpan w:val="2"/>
                                        <w:tcBorders>
                                            <w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:left w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                        </w:tcBorders>
                                        <w:shd w:val="clear" w:color="000000" w:fill="6F2F9F"/>
                                        <w:vAlign w:val="center"/>
                                        <w:hideMark/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                        <w:pPr>
                                            <w:widowControl/>
                                            <w:jc w:val="center"/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>学习成果</w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="3827" w:type="dxa"/>
                                        <w:gridSpan w:val="2"/>
                                        <w:tcBorders>
                                            <w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:left w:val="nil"/>
                                            <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                        </w:tcBorders>
                                        <w:shd w:val="clear" w:color="000000" w:fill="6F2F9F"/>
                                        <w:vAlign w:val="center"/>
                                        <w:hideMark/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                        <w:pPr>
                                            <w:widowControl/>
                                            <w:ind w:firstLineChars="100" w:firstLine="200"/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>学习任务、过程和观测</w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="1701" w:type="dxa"/>
                                        <w:vMerge w:val="restart"/>
                                        <w:tcBorders>
                                            <w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:left w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                        </w:tcBorders>
                                        <w:shd w:val="clear" w:color="000000" w:fill="6F2F9F"/>
                                        <w:vAlign w:val="center"/>
                                        <w:hideMark/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                        <w:pPr>
                                            <w:widowControl/>
                                            <w:jc w:val="center"/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>实际学习成果评测（学生达到的平均水平）</w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                            </w:tr>
                            <w:tr w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidTr="001E78CB">
                                <w:trPr>
                                    <w:trHeight w:val="1080"/>
                                </w:trPr>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="1843" w:type="dxa"/>
                                        <w:tcBorders>
                                            <w:top w:val="nil"/>
                                            <w:left w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                        </w:tcBorders>
                                        <w:shd w:val="clear" w:color="000000" w:fill="6F2F9F"/>
                                        <w:vAlign w:val="center"/>
                                        <w:hideMark/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                        <w:pPr>
                                            <w:widowControl/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>预设学习成果</w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="2126" w:type="dxa"/>
                                        <w:tcBorders>
                                            <w:top w:val="nil"/>
                                            <w:left w:val="nil"/>
                                            <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                        </w:tcBorders>
                                        <w:shd w:val="clear" w:color="000000" w:fill="6F2F9F"/>
                                        <w:vAlign w:val="center"/>
                                        <w:hideMark/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                        <w:pPr>
                                            <w:widowControl/>
                                            <w:jc w:val="center"/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>细</w:t>
                                        </w:r>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>化的</w:t>
                                        </w:r>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>预</w:t>
                                        </w:r>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>期</w:t>
                                        </w:r>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>学习</w:t>
                                        </w:r>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>成果及</w:t>
                                        </w:r>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>实</w:t>
                                        </w:r>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>施准</w:t>
                                        </w:r>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>则</w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="1418" w:type="dxa"/>
                                        <w:tcBorders>
                                            <w:top w:val="nil"/>
                                            <w:left w:val="nil"/>
                                            <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                        </w:tcBorders>
                                        <w:shd w:val="clear" w:color="000000" w:fill="6F2F9F"/>
                                        <w:vAlign w:val="center"/>
                                        <w:hideMark/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                        <w:pPr>
                                            <w:widowControl/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>预设的学习任务</w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="2409" w:type="dxa"/>
                                        <w:tcBorders>
                                            <w:top w:val="nil"/>
                                            <w:left w:val="nil"/>
                                            <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                        </w:tcBorders>
                                        <w:shd w:val="clear" w:color="000000" w:fill="6F2F9F"/>
                                        <w:vAlign w:val="center"/>
                                        <w:hideMark/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                        <w:pPr>
                                            <w:widowControl/>
                                            <w:jc w:val="center"/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="00CA7ABE">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                            <w:t>观测点</w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="1701" w:type="dxa"/>
                                        <w:vMerge/>
                                        <w:tcBorders>
                                            <w:top w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:left w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                        </w:tcBorders>
                                        <w:vAlign w:val="center"/>
                                        <w:hideMark/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                        <w:pPr>
                                            <w:widowControl/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei"/>
                                                <w:b/>
                                                <w:bCs/>
                                                <w:color w:val="FFFFFF"/>
                                                <w:sz w:val="20"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                        </w:pPr>
                                    </w:p>
                                </w:tc>
                            </w:tr>
                            [#--循环成绩组成列表，有多少成绩组成就有多少行--]
                            [#--用来标识是否进行合并，因为第一次循环课程目标下面的行要进行合并--]
                            [#assign aa =0]
                        [#list data.gradeconposeIndicationList as gradeconposeIndicationList ]
                            [#if gradeconposeIndicationList.indicationId == indicationList.indicationId]
                                <w:tr w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidTr="001E78CB">
                                    <w:trPr>
                                        <w:trHeight w:val="495"/>
                                    </w:trPr>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="1843" w:type="dxa"/>
                                            [#if aa==0]
                                                    <w:vMerge w:val="restart"/>
                                                [#else ]
                                                    <w:vMerge/>
                                            [/#if]
                                            <w:tcBorders>
                                                <w:top w:val="nil"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                            <w:hideMark/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                            <w:pPr>
                                                <w:widowControl/>
                                                <w:rPr>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="20"/>
                                                    <w:szCs w:val="20"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="00CA7ABE">
                                                <w:rPr>
                                                    <w:rFonts w:hint="eastAsia"/>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="20"/>
                                                    <w:szCs w:val="20"/>
                                                </w:rPr>
                                                [#if aa==0]
                                                    <w:t>${indicationList.content}</w:t>
                                                [/#if]
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="2126" w:type="dxa"/>
                                            [#if aa==0]
                                                <w:vMerge w:val="restart"/>
                                            [#else ]
                                                <w:vMerge/>
                                            [/#if]
                                            <w:tcBorders>
                                                <w:top w:val="nil"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                            <w:hideMark/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                            <w:pPr>
                                                <w:widowControl/>
                                                <w:rPr>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="20"/>
                                                    <w:szCs w:val="20"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="00CA7ABE">
                                                <w:rPr>
                                                    <w:rFonts w:hint="eastAsia"/>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="20"/>
                                                    <w:szCs w:val="20"/>
                                                </w:rPr>
                                                [#--只要找到  细化的预期学习成果及实施准则  就不再循环--]
                                                [#if aa=0]
                                                    [#if gradeconposeIndicationList.learnContent??]
                                                    <w:t>${gradeconposeIndicationList.learnContent} </w:t>
                                                     [/#if]
                                                [/#if]
                                                [#assign aa=1]
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    [#--下面就是成绩组成和观测点了--]
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="1418" w:type="dxa"/>
                                            <w:tcBorders>
                                                <w:top w:val="nil"/>
                                                <w:left w:val="nil"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                            <w:hideMark/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                            <w:pPr>
                                                <w:widowControl/>
                                                <w:rPr>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="20"/>
                                                    <w:szCs w:val="20"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="00CA7ABE">
                                                <w:rPr>
                                                    <w:rFonts w:hint="eastAsia"/>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="20"/>
                                                    <w:szCs w:val="20"/>
                                                </w:rPr>
                                                [#if gradeconposeIndicationList.gradecomposeName??]
                                                    <w:t>${gradeconposeIndicationList.gradecomposeName} </w:t>
                                                [/#if]

                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="2409" w:type="dxa"/>
                                            <w:tcBorders>
                                                <w:top w:val="nil"/>
                                                <w:left w:val="nil"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                            <w:hideMark/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                            <w:pPr>
                                                <w:widowControl/>
                                                <w:rPr>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="20"/>
                                                    <w:szCs w:val="20"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="00CA7ABE">
                                                <w:rPr>
                                                    <w:rFonts w:hint="eastAsia"/>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="20"/>
                                                    <w:szCs w:val="20"/>
                                                </w:rPr>
                                                [#if gradeconposeIndicationList.observeContent??]
                                                    <w:t>${gradeconposeIndicationList.observeContent} </w:t>
                                                [/#if]

                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="1701" w:type="dxa"/>
                                            <w:tcBorders>
                                                <w:top w:val="nil"/>
                                                <w:left w:val="nil"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="000000"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                            <w:hideMark/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00874A11" w:rsidRPr="00CA7ABE" w:rsidRDefault="00874A11" w:rsidP="001E78CB">
                                            <w:pPr>
                                                <w:widowControl/>
                                                <w:jc w:val="center"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="20"/>
                                                    <w:szCs w:val="20"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="00CA7ABE">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="20"/>
                                                    <w:szCs w:val="20"/>
                                                </w:rPr>
                                                <w:t>[#if indicationList.achieveValue??]${indicationList.achieveValue}[/#if]</w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                </w:tr>
                            [/#if]
                        [/#list]
                        </w:tbl>
                    [/#list]
                    [/#if]
                    [#--循环课程目标的评估结果结束--]
                    <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="00C53283">
                        <w:pPr>
                            <w:pStyle w:val="a3"/>
                            <w:spacing w:before="9"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei"/>
                                <w:b/>
                                <w:sz w:val="6"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="00162820">
                        <w:pPr>
                            <w:pStyle w:val="a5"/>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="2"/>
                            </w:numPr>
                            <w:tabs>
                                <w:tab w:val="left" w:pos="795"/>
                            </w:tabs>
                            <w:ind w:left="794" w:hanging="217"/>
                            <w:jc w:val="left"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                <w:b/>
                                <w:spacing w:val="-1"/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>课程学习目标的达成评价</w:t>
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="00B158A0">
                        <w:pPr>
                            <w:spacing w:before="1" w:line="472" w:lineRule="auto"/>
                            <w:ind w:right="469"/>
                            <w:rPr>
                                <w:sz w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>各学习目标预设的学习任务、实际学生达到平均水平、权重及加权后平均水平（达成度）如表</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="Times New Roman"/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>9</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>和图</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="Times New Roman"/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>3</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>所示。</w:t>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblW w:w="10080" w:type="dxa"/>
                            <w:tblInd w:w="-48" w:type="dxa"/>
                            <w:tblLayout w:type="fixed"/>
                            <w:tblCellMar>
                                <w:left w:w="57" w:type="dxa"/>
                                <w:right w:w="57" w:type="dxa"/>
                            </w:tblCellMar>
                            <w:tblLook w:val="04A0" w:firstRow="1" w:lastRow="0" w:firstColumn="1" w:lastColumn="0" w:noHBand="0" w:noVBand="1"/>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="315"/>
                            <w:gridCol w:w="525"/>
                            <w:gridCol w:w="158"/>
                            <w:gridCol w:w="52"/>
                            <w:gridCol w:w="525"/>
                            <w:gridCol w:w="840"/>
                            <w:gridCol w:w="381"/>
                            <w:gridCol w:w="459"/>
                            <w:gridCol w:w="261"/>
                            <w:gridCol w:w="209"/>
                            <w:gridCol w:w="370"/>
                            <w:gridCol w:w="321"/>
                            <w:gridCol w:w="304"/>
                            <w:gridCol w:w="777"/>
                            <w:gridCol w:w="231"/>
                            <w:gridCol w:w="770"/>
                            <w:gridCol w:w="327"/>
                            <w:gridCol w:w="292"/>
                            <w:gridCol w:w="540"/>
                            <w:gridCol w:w="638"/>
                            <w:gridCol w:w="262"/>
                            <w:gridCol w:w="578"/>
                            <w:gridCol w:w="945"/>
                        </w:tblGrid>
                        <w:tr w:rsidR="003F630F" w:rsidRPr="003F630F">
                            <w:trPr>
                                <w:trHeight w:val="452"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="998" w:type="dxa"/>
                                    <w:gridSpan w:val="3"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>课程名称</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1798" w:type="dxa"/>
                                    <w:gridSpan w:val="4"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>
                                            ${data.educlassCourseInfo.courseName}
                                        </w:t>
                                    </w:r>

                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="929" w:type="dxa"/>
                                    <w:gridSpan w:val="3"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>课程代码</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="995" w:type="dxa"/>
                                    <w:gridSpan w:val="3"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>${data.educlassCourseInfo.courseCode}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1008" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>学时</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="770" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>${data.educlassCourseInfo.allHours}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="619" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>学分</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="540" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>${data.educlassCourseInfo.credit}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="900" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>开课学期</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1523" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                            <w:u w:val="single"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>${data.educlassCourseInfo.courseTerm}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr w:rsidR="003F630F" w:rsidRPr="003F630F">
                            <w:trPr>
                                <w:trHeight w:val="457"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="998" w:type="dxa"/>
                                    <w:gridSpan w:val="3"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>专业班级</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1798" w:type="dxa"/>
                                    <w:gridSpan w:val="4"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>${data.educlassCourseInfo.educlassName}</w:t>
                                    </w:r>

                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="720" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>学生数</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="900" w:type="dxa"/>
                                    <w:gridSpan w:val="3"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>${data.educlassCourseInfo.studentNum}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1081" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>任课教师</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1620" w:type="dxa"/>
                                    <w:gridSpan w:val="4"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>${data.educlassCourseInfo.teacherName}</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1440" w:type="dxa"/>
                                    <w:gridSpan w:val="3"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>课程负责人</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1523" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>[#if data.educlassCourseInfo.teamLeader??]${data.educlassCourseInfo.teamLeader}[/#if]</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr w:rsidR="003F630F" w:rsidRPr="003F630F">
                            <w:trPr>
                                <w:trHeight w:val="457"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="998" w:type="dxa"/>
                                    <w:gridSpan w:val="3"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>主要教学文档</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="9082" w:type="dxa"/>
                                    <w:gridSpan w:val="20"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="both"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        [#--if判断在wps中占空格，所以写一行可以，word无所谓--]
                                        <w:t>[#if data.achieveReportInfo.teachDocument??]${data.achieveReportInfo.teachDocument}[/#if]</w:t>
                                    </w:r>

                                </w:p>
                            </w:tc>
                        </w:tr>
                        <w:tr w:rsidR="003F630F" w:rsidRPr="003F630F">
                            <w:trPr>
                                <w:trHeight w:val="61"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="315" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:tcMar>
                                        <w:left w:w="28" w:type="dxa"/>
                                        <w:right w:w="28" w:type="dxa"/>
                                    </w:tcMar>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>序号</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="735" w:type="dxa"/>
                                    <w:gridSpan w:val="3"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:tcMar>
                                        <w:left w:w="28" w:type="dxa"/>
                                        <w:right w:w="28" w:type="dxa"/>
                                    </w:tcMar>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>毕业要求指标点编号</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="525" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:tcMar>
                                        <w:left w:w="28" w:type="dxa"/>
                                        <w:right w:w="28" w:type="dxa"/>
                                    </w:tcMar>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>课程目标编号</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="840" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:tcMar>
                                        <w:left w:w="28" w:type="dxa"/>
                                        <w:right w:w="28" w:type="dxa"/>
                                    </w:tcMar>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>课程目标达成度期望值</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:i/>
                                            <w:iCs/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>E</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:i/>
                                            <w:iCs/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                            <w:vertAlign w:val="subscript"/>
                                        </w:rPr>
                                        <w:t>i</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="840" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:tcMar>
                                        <w:left w:w="28" w:type="dxa"/>
                                        <w:right w:w="28" w:type="dxa"/>
                                    </w:tcMar>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>课程目标达成度</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:i/>
                                            <w:iCs/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>ALCO</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:i/>
                                            <w:iCs/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                            <w:vertAlign w:val="subscript"/>
                                        </w:rPr>
                                        <w:t>i</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="840" w:type="dxa"/>
                                    <w:gridSpan w:val="3"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:tcMar>
                                        <w:left w:w="28" w:type="dxa"/>
                                        <w:right w:w="28" w:type="dxa"/>
                                    </w:tcMar>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>课程目标达成</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>否</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="2730" w:type="dxa"/>
                                    <w:gridSpan w:val="6"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:tcMar>
                                        <w:left w:w="28" w:type="dxa"/>
                                        <w:right w:w="28" w:type="dxa"/>
                                    </w:tcMar>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>达成途径</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>、</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>评价依据和评价</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>方法</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                            <w:vertAlign w:val="superscript"/>
                                        </w:rPr>
                                        <w:t>∆</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="1470" w:type="dxa"/>
                                    <w:gridSpan w:val="3"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:tcMar>
                                        <w:left w:w="28" w:type="dxa"/>
                                        <w:right w:w="28" w:type="dxa"/>
                                    </w:tcMar>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>指标点对应各课程目标达成度的最小值</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>(</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:i/>
                                            <w:iCs/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>ALCO</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                            <w:vertAlign w:val="subscript"/>
                                        </w:rPr>
                                        <w:t>min</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>)</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:i/>
                                            <w:iCs/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                            <w:vertAlign w:val="subscript"/>
                                        </w:rPr>
                                        <w:t>m</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="840" w:type="dxa"/>
                                    <w:gridSpan w:val="2"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:tcMar>
                                        <w:left w:w="28" w:type="dxa"/>
                                        <w:right w:w="28" w:type="dxa"/>
                                    </w:tcMar>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>课程权重</w:t>
                                    </w:r>
                                </w:p>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>（达成目标值）</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                            <w:i/>
                                            <w:iCs/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>W</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:i/>
                                            <w:iCs/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                            <w:vertAlign w:val="subscript"/>
                                        </w:rPr>
                                        <w:t>m</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="945" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                        <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                    </w:tcBorders>
                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                    <w:tcMar>
                                        <w:left w:w="28" w:type="dxa"/>
                                        <w:right w:w="28" w:type="dxa"/>
                                    </w:tcMar>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                    <w:pPr>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>毕业要求指标点达成度</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:i/>
                                            <w:iCs/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                        </w:rPr>
                                        <w:t>ALI</w:t>
                                    </w:r>
                                    <w:r w:rsidRPr="003F630F">
                                        <w:rPr>
                                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                            <w:i/>
                                            <w:iCs/>
                                            <w:kern w:val="0"/>
                                            <w:sz w:val="18"/>
                                            <w:szCs w:val="18"/>
                                            <w:vertAlign w:val="subscript"/>
                                        </w:rPr>
                                        <w:t>m</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [#assign oldPointId=0]
                        [#if data.indicationList ??  && data.indicationList?size>0]
                            [#list  data.indicationList as indicationList]
                                <w:tr w:rsidR="00A93330" w:rsidRPr="001743D2" w:rsidTr="00CF64C4">
                                    <w:trPr>
                                        <w:trHeight w:val="57"/>
                                    </w:trPr>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="315" w:type="dxa"/>
                                            <w:tcBorders>
                                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                            <w:pPr>
                                                <w:spacing w:line="240" w:lineRule="exact"/>
                                                <w:jc w:val="center"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="003F630F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>${indicationList.num}</w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="735" w:type="dxa"/>
                                            [#--合并列--]
                                            <w:gridSpan w:val="3"/>
                                            [#--合并行--]
                                            [#list data.indicationPointAndAceieveList as indicationPointAndAceieveList ]
                                            [#--megeNum>1需要合并单元格 老的id不等于新的指标点id--]
                                            [#--先判断这个指标点是否需要合并--]
                                                [#if indicationPointAndAceieveList.pointId == indicationList.pointId && indicationPointAndAceieveList.megeNum>1 ]
                                                [#--在判断老的id与新的id是否相同，不相同则新的合并,oldPointId在循环最后一个单元格赋值--]
                                                    [#if  oldPointId !=indicationList.pointId]
                                                        <w:vMerge w:val="restart"/>
                                                    [#else ]
                                                        <w:vMerge/>
                                                    [/#if]
                                                [/#if]
                                            [/#list]
                                            <w:tcBorders>
                                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                            <w:pPr>
                                                <w:spacing w:line="240" w:lineRule="exact"/>
                                                <w:jc w:val="center"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="003F630F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>
                                                    [#if  oldPointId !=indicationList.pointId]
                                                        [#list data.indicationPointList as indicationPointList ]
                                                            [#if indicationPointList.pointId==indicationList.pointId ]
                                                                ${indicationPointList.graduateIndexNum}.${indicationPointList.index_num}
                                                            [/#if]
                                                        [/#list]
                                                        [#assign mageState=2]
                                                    [/#if]
                                                </w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="525" w:type="dxa"/>
                                            <w:tcBorders>
                                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                            <w:pPr>
                                                <w:spacing w:line="240" w:lineRule="exact"/>
                                                <w:jc w:val="center"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="003F630F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t xml:space="preserve">CO${indicationList.sort} </w:t>
                                            </w:r>

                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="840" w:type="dxa"/>
                                            <w:tcBorders>
                                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                            <w:pPr>
                                                <w:spacing w:line="240" w:lineRule="exact"/>
                                                <w:jc w:val="center"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="003F630F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>${indicationList.expected_value}</w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="840" w:type="dxa"/>
                                            <w:gridSpan w:val="2"/>
                                            <w:tcBorders>
                                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                            <w:pPr>
                                                <w:spacing w:line="240" w:lineRule="exact"/>
                                                <w:jc w:val="center"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="003F630F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>${indicationList.achieveValue}</w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="840" w:type="dxa"/>
                                            <w:gridSpan w:val="3"/>
                                            <w:tcBorders>
                                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                            <w:pPr>
                                                <w:spacing w:line="240" w:lineRule="exact"/>
                                                <w:jc w:val="center"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="003F630F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>${indicationList.isAchieve}</w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="3433" w:type="dxa"/>
                                            <w:gridSpan w:val="6"/>
                                            <w:tcBorders>
                                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                            <w:pPr>
                                                <w:widowControl/>
                                                <w:numPr>
                                                    <w:ilvl w:val="0"/>
                                                    <w:numId w:val="4"/>
                                                </w:numPr>
                                                <w:autoSpaceDE/>
                                                <w:autoSpaceDN/>
                                                <w:spacing w:line="240" w:lineRule="exact"/>
                                                <w:ind w:left="153" w:hanging="153"/>
                                                <w:jc w:val="both"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="001743D2">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>达成途径：</w:t>
                                            </w:r>

                                            <w:r w:rsidRPr="003F630F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>[#if indicationList.reachWay??]${indicationList.reachWay}[/#if]</w:t>
                                            </w:r>
                                        </w:p>
                                        <w:p w:rsidR="00A93330" w:rsidRPr="001743D2" w:rsidRDefault="00A93330" w:rsidP="00162820">
                                            <w:pPr>
                                                <w:widowControl/>
                                                <w:numPr>
                                                    <w:ilvl w:val="0"/>
                                                    <w:numId w:val="4"/>
                                                </w:numPr>
                                                <w:autoSpaceDE/>
                                                <w:autoSpaceDN/>
                                                <w:spacing w:line="240" w:lineRule="exact"/>
                                                <w:ind w:left="153" w:hanging="153"/>
                                                <w:jc w:val="both"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="001743D2">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>评价依据：</w:t>
                                            </w:r>
                                            <w:r w:rsidRPr="003F630F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>[#if indicationList.assessGist??]${indicationList.assessGist}[/#if]</w:t>
                                            </w:r>
                                        </w:p>
                                        <w:p w:rsidR="00A93330" w:rsidRPr="001743D2" w:rsidRDefault="00A93330" w:rsidP="00162820">
                                            <w:pPr>
                                                <w:widowControl/>
                                                <w:numPr>
                                                    <w:ilvl w:val="0"/>
                                                    <w:numId w:val="4"/>
                                                </w:numPr>
                                                <w:autoSpaceDE/>
                                                <w:autoSpaceDN/>
                                                <w:spacing w:line="240" w:lineRule="exact"/>
                                                <w:ind w:left="153" w:hanging="153"/>
                                                <w:jc w:val="both"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="001743D2">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:color w:val="000000"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>评价方法：</w:t>
                                            </w:r>
                                            <w:r w:rsidRPr="003F630F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>[#if indicationList.assessMethod??]${indicationList.assessMethod}[/#if]</w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="1470" w:type="dxa"/>
                                            <w:gridSpan w:val="3"/>
                                            [#list data.indicationPointAndAceieveList as indicationPointAndAceieveList ]
                                                [#list data.indicationPointAndAceieveList as indicationPointAndAceieveList ]
                                                [#--megeNum>1需要合并单元格 老的id不等于新的指标点id--]
                                                [#--先判断这个指标点是否需要合并--]
                                                    [#if indicationPointAndAceieveList.pointId == indicationList.pointId && indicationPointAndAceieveList.megeNum>1 ]
                                                    [#--在判断老的id与新的id是否相同，不相同则新的合并,oldPointId在循环最后一个单元格赋值--]
                                                        [#if  oldPointId !=indicationList.pointId]
                                                            <w:vMerge w:val="restart"/>
                                                        [#else ]
                                                            <w:vMerge/>
                                                        [/#if]
                                                    [/#if]
                                                [/#list]
                                            [/#list]
                                            [#--<w:vMerge w:val="restart"/>--]
                                            <w:tcBorders>
                                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                            <w:pPr>
                                                <w:spacing w:line="240" w:lineRule="exact"/>
                                                <w:jc w:val="center"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="003F630F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>
                                                    [#if  oldPointId !=indicationList.pointId]
                                                        [#list data.indicationPointAndAceieveList as indicationPointAndAceieveList ]
                                                            [#if indicationPointAndAceieveList.pointId==indicationList.pointId ]
                                                                ${indicationPointAndAceieveList.achieve}
                                                            [/#if]
                                                        [/#list]
                                                        [#assign mageState=2]
                                                    [/#if]
                                                </w:t>
                                            </w:r>

                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="840" w:type="dxa"/>
                                            <w:gridSpan w:val="2"/>
                                            [#list data.indicationPointAndAceieveList as indicationPointAndAceieveList ]
                                                [#list data.indicationPointAndAceieveList as indicationPointAndAceieveList ]
                                                [#--megeNum>1需要合并单元格 老的id不等于新的指标点id--]
                                                [#--先判断这个指标点是否需要合并--]
                                                    [#if indicationPointAndAceieveList.pointId == indicationList.pointId && indicationPointAndAceieveList.megeNum>1 ]
                                                    [#--在判断老的id与新的id是否相同，不相同则新的合并,oldPointId在循环最后一个单元格赋值--]
                                                        [#if  oldPointId !=indicationList.pointId]
                                                            <w:vMerge w:val="restart"/>
                                                        [#else ]
                                                            <w:vMerge/>
                                                        [/#if]
                                                    [/#if]
                                                [/#list]
                                            [/#list]
                                            [#--<w:vMerge w:val="restart"/>--]
                                            <w:tcBorders>
                                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                            <w:pPr>
                                                <w:spacing w:line="240" w:lineRule="exact"/>
                                                <w:jc w:val="center"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="003F630F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>
                                                    [#if  oldPointId !=indicationList.pointId]
                                                        [#list data.indicationPointAndAceieveList as indicationPointAndAceieveList ]
                                                            [#if indicationPointAndAceieveList.pointId==indicationList.pointId ]
                                                                ${indicationPointAndAceieveList.weight}
                                                            [/#if]
                                                        [/#list]
                                                        [#assign mageState=2]
                                                    [/#if]
                                                </w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="945" w:type="dxa"/>
                                            [#list data.indicationPointAndAceieveList as indicationPointAndAceieveList ]
                                                [#list data.indicationPointAndAceieveList as indicationPointAndAceieveList ]
                                                [#--megeNum>1需要合并单元格 老的id不等于新的指标点id--]
                                                [#--先判断这个指标点是否需要合并--]
                                                    [#if indicationPointAndAceieveList.pointId == indicationList.pointId && indicationPointAndAceieveList.megeNum>1 ]
                                                    [#--在判断老的id与新的id是否相同，不相同则新的合并,oldPointId在循环最后一个单元格赋值--]
                                                        [#if  oldPointId !=indicationList.pointId]
                                                            <w:vMerge w:val="restart"/>
                                                        [#else ]
                                                            <w:vMerge/>
                                                        [/#if]
                                                    [/#if]
                                                [/#list]
                                            [/#list]
                                            [#--<w:vMerge w:val="restart"/>--]
                                            <w:tcBorders>
                                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                            </w:tcBorders>
                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                            <w:vAlign w:val="center"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00AC0F3E" w:rsidRPr="003F630F" w:rsidRDefault="00ED74D0">
                                            <w:pPr>
                                                <w:spacing w:line="240" w:lineRule="exact"/>
                                                <w:jc w:val="center"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="003F630F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:eastAsia="楷体" w:hAnsi="Times New Roman" w:hint="eastAsia"/>
                                                    <w:kern w:val="0"/>
                                                    <w:sz w:val="18"/>
                                                    <w:szCs w:val="18"/>
                                                </w:rPr>
                                                <w:t>
                                                    [#if  oldPointId !=indicationList.pointId]
                                                        [#list data.indicationPointAndAceieveList as indicationPointAndAceieveList ]
                                                            [#if indicationPointAndAceieveList.pointId==indicationList.pointId ]
                                                                ${indicationPointAndAceieveList.pointAchieve}
                                                            [/#if]
                                                        [/#list]
                                                        [#assign mageState=2]
                                                    [/#if]
                                                    [#--赋值给老的id--]
                                                    [#assign oldPointId =indicationList.pointId]
                                                </w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                </w:tr>
                            [/#list]
                        [/#if]
                    </w:tbl>

                    <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="00C53283">
                        <w:pPr>
                            <w:pStyle w:val="a3"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                            </w:rPr>
                        </w:pPr>
                        <w:bookmarkStart w:id="1" w:name="_GoBack"/>
                        <w:bookmarkEnd w:id="1"/>
                    </w:p>
                    [#--本课程课程目标达成度的图片位置--]
                    [#if data.imageList??]
                        [#list data.imageList as imageList]
                            [#if imageList.type==1]
                                <w:p w:rsidR="00DA2E64" w:rsidRDefault="00FF00E8">
                                    <w:bookmarkStart w:id="0" w:name="_GoBack"/>
                                    <w:r w:rsidRPr="00FF00E8">
                                        <w:rPr>
                                            <w:noProof/>
                                        </w:rPr>
                                        <w:drawing>
                                            <wp:inline distT="0" distB="0" distL="0" distR="0">
                                                <wp:extent cx="5274310" cy="2966799"/>
                                                <wp:effectExtent l="0" t="0" r="2540" b="5080"/>
                                                <wp:docPr id="1" name="图片 1" descr="C:\Users\0000\Pictures\0.jpg"/>
                                                <wp:cNvGraphicFramePr>
                                                    <a:graphicFrameLocks
                                                            xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" noChangeAspect="1"/>
                                                </wp:cNvGraphicFramePr>
                                                <a:graphic
                                                        xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                                    <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                        <pic:pic
                                                                xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                            <pic:nvPicPr>
                                                                <pic:cNvPr id="0" name="Picture 1" descr="C:\Users\0000\Pictures\0.jpg"/>
                                                                <pic:cNvPicPr>
                                                                    <a:picLocks noChangeAspect="1" noChangeArrowheads="1"/>
                                                                </pic:cNvPicPr>
                                                            </pic:nvPicPr>
                                                            <pic:blipFill>
                                                                <a:blip r:embed="${imageList.imageUrl}" cstate="print">
                                                                    <a:extLst>
                                                                        <a:ext uri="{28A0092B-C50C-407E-A947-70E740481C1C}">
                                                                            <a14:useLocalDpi
                                                                                    xmlns:a14="http://schemas.microsoft.com/office/drawing/2010/main" val="0"/>
                                                                        </a:ext>
                                                                    </a:extLst>
                                                                </a:blip>
                                                                <a:srcRect/>
                                                                <a:stretch>
                                                                    <a:fillRect/>
                                                                </a:stretch>
                                                            </pic:blipFill>
                                                            <pic:spPr bwMode="auto">
                                                                <a:xfrm>
                                                                    <a:off x="0" y="0"/>
                                                                    <a:ext cx="5274310" cy="2966799"/>
                                                                </a:xfrm>
                                                                <a:prstGeom prst="rect">
                                                                    <a:avLst/>
                                                                </a:prstGeom>
                                                                <a:noFill/>
                                                                <a:ln>
                                                                    <a:noFill/>
                                                                </a:ln>
                                                            </pic:spPr>
                                                        </pic:pic>
                                                    </a:graphicData>
                                                </a:graphic>
                                            </wp:inline>
                                        </w:drawing>
                                    </w:r>
                                    <w:bookmarkEnd w:id="0"/>
                                </w:p>
                            [/#if]
                        [/#list]

                    [/#if]

                    <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="00C53283">
                        <w:pPr>
                            <w:spacing w:before="73"/>
                            <w:ind w:left="2"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>图</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="Times New Roman"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve">3 </w:t>
                        </w:r>
                        <w:r w:rsidR="0051427C">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsiaTheme="minorEastAsia"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>${data.educlassCourseInfo.courseTerm}</w:t>
                        </w:r>
                        <w:r w:rsidR="0051427C">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsiaTheme="minorEastAsia" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>学年</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="21"/>
                            </w:rPr>
                            <w:t>课程目标达成度</w:t>
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="00BD26DB" w:rsidRDefault="00BD26DB"/>
                    <w:p w:rsidR="00A93330" w:rsidRPr="006E34ED" w:rsidRDefault="00A93330" w:rsidP="00A93330">
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>4</w:t>
                        </w:r>
                        <w:r w:rsidRPr="006E34ED">
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>、课程目标达成度评价结果的分析</w:t>
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="00741DEA" w:rsidRDefault="00A93330" w:rsidP="00741DEA">
                        <w:pPr>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:ind w:firstLine="420"/>
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                            </w:rPr>
                            <w:t>根据本专业课程目标达成度计算方法</w:t>
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="00835551" w:rsidRPr="00835551" w:rsidRDefault="00835551" w:rsidP="00835551">
                        <w:pPr>
                            <w:autoSpaceDE/>
                            <w:autoSpaceDN/>
                            <w:spacing w:line="480" w:lineRule="exact"/>
                            <w:ind w:firstLine="420"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:cs="Times New Roman"/>
                                <w:kern w:val="2"/>
                                <w:sz w:val="21"/>
                                <w:lang w:val="en-US" w:bidi="ar-SA"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="00835551">
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:kern w:val="2"/>
                                <w:sz w:val="21"/>
                                <w:lang w:val="en-US" w:bidi="ar-SA"/>
                            </w:rPr>
                            <w:t>表3 各目标达成情况分析</w:t>
                        </w:r>
                    </w:p>
                    <w:tbl>
                        <w:tblPr>
                            <w:tblpPr w:leftFromText="180" w:rightFromText="180" w:vertAnchor="text" w:horzAnchor="margin" w:tblpXSpec="center" w:tblpY="157"/>
                            <w:tblW w:w="8222" w:type="dxa"/>
                            <w:tblBorders>
                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:insideH w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                <w:insideV w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            </w:tblBorders>
                            <w:tblLayout w:type="fixed"/>
                            <w:tblLook w:val="04A0" w:firstRow="1" w:lastRow="0" w:firstColumn="1" w:lastColumn="0" w:noHBand="0" w:noVBand="1"/>
                        </w:tblPr>
                        <w:tblGrid>
                            <w:gridCol w:w="3686"/>
                            <w:gridCol w:w="4536"/>
                        </w:tblGrid>
                        <w:tr w:rsidR="00146544">
                            <w:trPr>
                                <w:trHeight w:val="291"/>
                            </w:trPr>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="3686" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:tl2br w:val="nil"/>
                                        <w:tr2bl w:val="nil"/>
                                    </w:tcBorders>
                                    <w:vAlign w:val="center"/>
                                </w:tcPr>
                                <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                    <w:pPr>
                                        <w:spacing w:line="480" w:lineRule="exact"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:cs="Times New Roman"/>
                                            <w:b/>
                                            <w:szCs w:val="24"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:cs="Times New Roman"/>
                                            <w:b/>
                                            <w:szCs w:val="24"/>
                                        </w:rPr>
                                        <w:lastRenderedPageBreak/>
                                        <w:t>课程目标</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                            <w:tc>
                                <w:tcPr>
                                    <w:tcW w:w="4536" w:type="dxa"/>
                                    <w:tcBorders>
                                        <w:tl2br w:val="nil"/>
                                        <w:tr2bl w:val="nil"/>
                                    </w:tcBorders>
                                </w:tcPr>
                                <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                    <w:pPr>
                                        <w:spacing w:line="480" w:lineRule="exact"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:cs="Times New Roman"/>
                                            <w:b/>
                                            <w:szCs w:val="24"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:cs="Times New Roman" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:szCs w:val="24"/>
                                        </w:rPr>
                                        <w:t>分析</w:t>
                                    </w:r>
                                </w:p>
                            </w:tc>
                        </w:tr>
                        [#list data.courseAllIndication as indicationList]
                            <w:tr w:rsidR="00146544">
                                <w:trPr>
                                    <w:trHeight w:val="50"/>
                                </w:trPr>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="3686" w:type="dxa"/>
                                        <w:tcBorders>
                                            <w:tl2br w:val="nil"/>
                                            <w:tr2bl w:val="nil"/>
                                        </w:tcBorders>
                                        <w:vAlign w:val="center"/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                        <w:pPr>
                                            <w:spacing w:line="480" w:lineRule="exact"/>
                                            <w:jc w:val="left"/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                            </w:rPr>
                                            <w:t xml:space="preserve">目标${indicationList.sort}. ${indicationList.content}</w:t>
                                        </w:r>

                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="4536" w:type="dxa"/>
                                        <w:tcBorders>
                                            <w:tl2br w:val="nil"/>
                                            <w:tr2bl w:val="nil"/>
                                        </w:tcBorders>
                                    </w:tcPr>
                                    <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                        <w:pPr>
                                            <w:spacing w:line="480" w:lineRule="exact"/>
                                            <w:jc w:val="left"/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                            </w:rPr>
                                            [#if indicationList.achieveValue??]
                                                <w:t>达成度为${indicationList.achieveValue}[#if indicationList.indicationAnalyze??],${indicationList.indicationAnalyze}[/#if]
                                                </w:t>
                                            [/#if]
                                        </w:r>
                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            </w:rPr>
                                            <w:t xml:space="preserve"> </w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                            </w:tr>
                        [/#list]

                    </w:tbl>
                    <w:p w:rsidR="00741DEA" w:rsidRDefault="00741DEA" w:rsidP="00835551">
                        <w:pPr>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w:rsidR="00741DEA" w:rsidRPr="00741DEA" w:rsidRDefault="00741DEA" w:rsidP="00741DEA">
                        <w:pPr>
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>5</w:t>
                        </w:r>
                        <w:r w:rsidRPr="00AB4FFA">
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>、针对个</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>体</w:t>
                        </w:r>
                        <w:r w:rsidRPr="00AB4FFA">
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                <w:sz w:val="24"/>
                                <w:szCs w:val="24"/>
                            </w:rPr>
                            <w:t>的达成度评价分析</w:t>
                        </w:r>
                    </w:p>
                    [#if data.achieveReportInfo.personAchieveAnalyze??]
                        [#assign mainContentMap = data.achieveReportInfo.personAchieveAnalyze]
                        [#if mainContentMap?is_hash]
                            [#if mainContentMap.isNonChinese == 0]
                                <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                    <w:pPr>
                                        <w:spacing w:line="480" w:lineRule="exact"/>
                                        <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                        </w:rPr>
                                    </w:pPr>

                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                        </w:rPr>
                                        <w:t>
                                            [#--[#if data.achieveReportInfo.targetRequire??]
                                                ${data.achieveReportInfo.targetRequire}
                                            [/#if]--]
                                        </w:t>
                                    </w:r>
                                </w:p>
                            [#else]
                                [#assign paragraphDataList = mainContentMap.paragraphDataList]
                                [#if paragraphDataList?? && paragraphDataList?size >0]
                                    [#list paragraphDataList as paragraph]
                                        <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                            <w:pPr>
                                                <w:spacing w:line="480" w:lineRule="exact"/>
                                                <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                                </w:rPr>
                                            </w:pPr>
                                            [#if paragraph?? && paragraph?size > 0]
                                                [#list paragraph as sentence]
                                                    [#if sentence?? && sentence.isNonChinese??]
                                                        [#if sentence.isNonChinese == 1]
                                                            <w:r>
                                                                <w:rPr>
                                                                    <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                                                </w:rPr>
                                                                <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                            </w:r>
                                                        [#else ]
                                                            <w:r>
                                                                <w:rPr>
                                                                    <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                                                </w:rPr>
                                                                <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                            </w:r>
                                                        [/#if]
                                                    [/#if]
                                                [/#list]
                                            [/#if]
                                        </w:p>
                                    [/#list]
                                [/#if]
                            [/#if]
                        [/#if]
                    [/#if]
                    [#--本课程各成绩组成的成绩散点图图片位置--]
                    [#if data.imageList??]
                        [#list data.imageList as imageList]
                            [#if imageList.type==3]
                                <w:p w:rsidR="00DA2E64" w:rsidRDefault="00FF00E8">
                                    <w:bookmarkStart w:id="0" w:name="_GoBack"/>
                                    <w:r w:rsidRPr="00FF00E8">
                                        <w:rPr>
                                            <w:noProof/>
                                        </w:rPr>
                                        <w:drawing>
                                            <wp:inline distT="0" distB="0" distL="0" distR="0">
                                                <wp:extent cx="5274310" cy="2966799"/>
                                                <wp:effectExtent l="0" t="0" r="2540" b="5080"/>
                                                <wp:docPr id="1" name="图片 1" descr="C:\Users\0000\Pictures\0.jpg"/>
                                                <wp:cNvGraphicFramePr>
                                                    <a:graphicFrameLocks
                                                            xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" noChangeAspect="1"/>
                                                </wp:cNvGraphicFramePr>
                                                <a:graphic
                                                        xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                                    <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                        <pic:pic
                                                                xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                            <pic:nvPicPr>
                                                                <pic:cNvPr id="0" name="Picture 1" descr="C:\Users\0000\Pictures\0.jpg"/>
                                                                <pic:cNvPicPr>
                                                                    <a:picLocks noChangeAspect="1" noChangeArrowheads="1"/>
                                                                </pic:cNvPicPr>
                                                            </pic:nvPicPr>
                                                            <pic:blipFill>
                                                                <a:blip r:embed="${imageList.imageUrl}" cstate="print">
                                                                    <a:extLst>
                                                                        <a:ext uri="{28A0092B-C50C-407E-A947-70E740481C1C}">
                                                                            <a14:useLocalDpi
                                                                                    xmlns:a14="http://schemas.microsoft.com/office/drawing/2010/main" val="0"/>
                                                                        </a:ext>
                                                                    </a:extLst>
                                                                </a:blip>
                                                                <a:srcRect/>
                                                                <a:stretch>
                                                                    <a:fillRect/>
                                                                </a:stretch>
                                                            </pic:blipFill>
                                                            <pic:spPr bwMode="auto">
                                                                <a:xfrm>
                                                                    <a:off x="0" y="0"/>
                                                                    <a:ext cx="5274310" cy="2966799"/>
                                                                </a:xfrm>
                                                                <a:prstGeom prst="rect">
                                                                    <a:avLst/>
                                                                </a:prstGeom>
                                                                <a:noFill/>
                                                                <a:ln>
                                                                    <a:noFill/>
                                                                </a:ln>
                                                            </pic:spPr>
                                                        </pic:pic>
                                                    </a:graphicData>
                                                </a:graphic>
                                            </wp:inline>
                                        </w:drawing>
                                    </w:r>

                                    <w:bookmarkEnd w:id="0"/>

                                </w:p>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="00C53283">
                                    <w:pPr>
                                        <w:spacing w:before="73"/>
                                        <w:ind w:left="2"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:sz w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if imageList.imageName??]${imageList.imageName}[/#if]</w:t>
                                    </w:r>


                                </w:p>
                            [/#if]
                        [/#list]

                    [/#if]
                    <w:p w:rsidR="00976D72" w:rsidRPr="0096637C" w:rsidRDefault="00976D72" w:rsidP="00162820">
                        <w:pPr>
                            <w:pStyle w:val="a5"/>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="5"/>
                            </w:numPr>
                            <w:autoSpaceDE/>
                            <w:autoSpaceDN/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="0096637C">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:t>本次教学改进</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:t>的措施和效果</w:t>
                        </w:r>
                    </w:p>
                    [#if data.achieveReportInfo.teachModified??]
                        [#assign mainContentMap = data.achieveReportInfo.teachModified]
                        [#if mainContentMap?is_hash]
                            [#if mainContentMap.isNonChinese == 0]
                                <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                    <w:pPr>
                                        <w:spacing w:line="480" w:lineRule="exact"/>
                                        <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                        </w:rPr>
                                    </w:pPr>

                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                        </w:rPr>
                                        <w:t>
                                            [#--[#if data.achieveReportInfo.targetRequire??]
                                                ${data.achieveReportInfo.targetRequire}
                                            [/#if]--]
                                        </w:t>
                                    </w:r>
                                </w:p>
                            [#else]
                                [#assign paragraphDataList = mainContentMap.paragraphDataList]
                                [#if paragraphDataList?? && paragraphDataList?size >0]
                                    [#list paragraphDataList as paragraph]
                                    <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                        <w:pPr>
                                            <w:spacing w:line="480" w:lineRule="exact"/>
                                            <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            </w:rPr>
                                        </w:pPr>
                                        [#if paragraph?? && paragraph?size > 0]
                                            [#list paragraph as sentence]
                                                [#if sentence?? && sentence.isNonChinese??]
                                                    [#if sentence.isNonChinese == 1]
                                                        <w:r>
                                                            <w:rPr>
                                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                                            </w:rPr>
                                                            <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                        </w:r>
                                                    [#else ]
                                                        <w:r>
                                                            <w:rPr>
                                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                                            </w:rPr>
                                                            <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                        </w:r>
                                                    [/#if]
                                                [/#if]
                                            [/#list]
                                        [/#if]
                                    </w:p>
                                    [/#list]
                                [/#if]
                            [/#if]
                        [/#if]
                    [/#if]
                    [#--本课程历年课程目标对比图片位置--]
                    [#if data.imageList??]
                        [#list data.imageList as imageList]
                            [#if imageList.type==2]
                                <w:p w:rsidR="00DA2E64" w:rsidRDefault="00FF00E8">
                                    <w:bookmarkStart w:id="0" w:name="_GoBack"/>
                                    <w:r w:rsidRPr="00FF00E8">
                                        <w:rPr>
                                            <w:noProof/>
                                        </w:rPr>
                                        <w:drawing>
                                            <wp:inline distT="0" distB="0" distL="0" distR="0">
                                                <wp:extent cx="5274310" cy="2966799"/>
                                                <wp:effectExtent l="0" t="0" r="2540" b="5080"/>
                                                <wp:docPr id="1" name="图片 1" descr="C:\Users\0000\Pictures\0.jpg"/>
                                                <wp:cNvGraphicFramePr>
                                                    <a:graphicFrameLocks
                                                            xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" noChangeAspect="1"/>
                                                </wp:cNvGraphicFramePr>
                                                <a:graphic
                                                        xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                                    <a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                        <pic:pic
                                                                xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture">
                                                            <pic:nvPicPr>
                                                                <pic:cNvPr id="0" name="Picture 1" descr="C:\Users\0000\Pictures\0.jpg"/>
                                                                <pic:cNvPicPr>
                                                                    <a:picLocks noChangeAspect="1" noChangeArrowheads="1"/>
                                                                </pic:cNvPicPr>
                                                            </pic:nvPicPr>
                                                            <pic:blipFill>
                                                                <a:blip r:embed="${imageList.imageUrl}" cstate="print">
                                                                    <a:extLst>
                                                                        <a:ext uri="{28A0092B-C50C-407E-A947-70E740481C1C}">
                                                                            <a14:useLocalDpi
                                                                                    xmlns:a14="http://schemas.microsoft.com/office/drawing/2010/main" val="0"/>
                                                                        </a:ext>
                                                                    </a:extLst>
                                                                </a:blip>
                                                                <a:srcRect/>
                                                                <a:stretch>
                                                                    <a:fillRect/>
                                                                </a:stretch>
                                                            </pic:blipFill>
                                                            <pic:spPr bwMode="auto">
                                                                <a:xfrm>
                                                                    <a:off x="0" y="0"/>
                                                                    <a:ext cx="5274310" cy="2966799"/>
                                                                </a:xfrm>
                                                                <a:prstGeom prst="rect">
                                                                    <a:avLst/>
                                                                </a:prstGeom>
                                                                <a:noFill/>
                                                                <a:ln>
                                                                    <a:noFill/>
                                                                </a:ln>
                                                            </pic:spPr>
                                                        </pic:pic>
                                                    </a:graphicData>
                                                </a:graphic>
                                            </wp:inline>
                                        </w:drawing>
                                    </w:r>

                                    <w:bookmarkEnd w:id="0"/>

                                </w:p>
                                <w:p w:rsidR="00C53283" w:rsidRDefault="00C53283" w:rsidP="00C53283">
                                    <w:pPr>
                                        <w:spacing w:before="73"/>
                                        <w:ind w:left="2"/>
                                        <w:jc w:val="center"/>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei"/>
                                            <w:b/>
                                            <w:sz w:val="21"/>
                                        </w:rPr>
                                    </w:pPr>
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hint="eastAsia"/>
                                            <w:b/>
                                            <w:sz w:val="21"/>
                                        </w:rPr>
                                        <w:t>[#if imageList.imageName??]${imageList.imageName}[/#if]</w:t>
                                    </w:r>


                                </w:p>
                            [/#if]
                        [/#list]

                    [/#if]

                    <w:p w:rsidR="00976D72" w:rsidRPr="00484167" w:rsidRDefault="00976D72" w:rsidP="00162820">
                        <w:pPr>
                            <w:pStyle w:val="a5"/>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="5"/>
                            </w:numPr>
                            <w:autoSpaceDE/>
                            <w:autoSpaceDN/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="00484167">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:t>存在的问题与持续改进</w:t>
                        </w:r>
                    </w:p>
                    [#if data.achieveReportInfo.problemModified??]
                    [#--富文本--]
                        [#if data.achieveReportInfo.problemModified?is_string && data.achieveReportInfo.problemModified?index_of("<w:") != -1 && data.achieveReportInfo.problemModified?index_of("</w:") != -1]
                            ${data.achieveReportInfo.problemModified}
                        [#else ]
                        [#--下面就是是否有回车 有换行 自成一段--]
                            [#assign mainContentMap = data.achieveReportInfo.problemModified]
                            [#if mainContentMap?is_hash]
                                [#if mainContentMap.isNonChinese == 0]
                                    <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                        <w:pPr>
                                            <w:spacing w:line="480" w:lineRule="exact"/>
                                            <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            </w:rPr>
                                        </w:pPr>

                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                            </w:rPr>
                                            <w:t>
                                                [#--[#if data.achieveReportInfo.targetRequire??]
                                                    ${data.achieveReportInfo.targetRequire}
                                                [/#if]--]
                                            </w:t>
                                        </w:r>
                                    </w:p>
                                [#else]
                                    [#assign paragraphDataList = mainContentMap.paragraphDataList]
                                    [#if paragraphDataList?? && paragraphDataList?size >0]
                                        [#list paragraphDataList as paragraph]
                                            <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                                <w:pPr>
                                                    <w:spacing w:line="480" w:lineRule="exact"/>
                                                    <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                    <w:rPr>
                                                        <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                                    </w:rPr>
                                                </w:pPr>
                                                [#if paragraph?? && paragraph?size > 0]
                                                    [#list paragraph as sentence]
                                                        [#if sentence?? && sentence.isNonChinese??]
                                                            [#if sentence.isNonChinese == 1]
                                                                <w:r>
                                                                    <w:rPr>
                                                                        <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                                                    </w:rPr>
                                                                    <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                </w:r>
                                                            [#else ]
                                                                <w:r>
                                                                    <w:rPr>
                                                                        <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                                                    </w:rPr>
                                                                    <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                </w:r>
                                                            [/#if]
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                            </w:p>
                                        [/#list]
                                    [/#if]
                                [/#if]
                            [/#if]
                        [/#if]
                    [/#if]
                    <w:p w:rsidR="00976D72" w:rsidRDefault="00976D72" w:rsidP="00162820">
                        <w:pPr>
                            <w:pStyle w:val="a5"/>
                            <w:numPr>
                                <w:ilvl w:val="0"/>
                                <w:numId w:val="5"/>
                            </w:numPr>
                            <w:autoSpaceDE/>
                            <w:autoSpaceDN/>
                            <w:jc w:val="both"/>
                            <w:rPr>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="00C362FB">
                            <w:rPr>
                                <w:rFonts w:hint="eastAsia"/>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                            <w:t>需要反馈的问题</w:t>
                        </w:r>
                    </w:p>
                    [#if data.achieveReportInfo.problemContent??]
                    [#--富文本--]
                        [#if data.achieveReportInfo.problemContent?is_string && data.achieveReportInfo.problemContent?index_of("<w:") != -1 && data.achieveReportInfo.problemContent?index_of("</w:") != -1]
                            ${data.achieveReportInfo.problemContent}
                        [#else ]
                        [#--下面就是是否有回车 有换行 自成一段--]
                            [#assign mainContentMap = data.achieveReportInfo.problemContent]
                            [#if mainContentMap?is_hash]
                                [#if mainContentMap.isNonChinese == 0]
                                    <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                        <w:pPr>
                                            <w:spacing w:line="480" w:lineRule="exact"/>
                                            <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                            </w:rPr>
                                        </w:pPr>

                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                            </w:rPr>
                                            <w:t>
                                                [#--[#if data.achieveReportInfo.targetRequire??]
                                                    ${data.achieveReportInfo.targetRequire}
                                                [/#if]--]
                                            </w:t>
                                        </w:r>
                                    </w:p>
                                [#else]
                                    [#assign paragraphDataList = mainContentMap.paragraphDataList]
                                    [#if paragraphDataList?? && paragraphDataList?size >0]
                                        [#list paragraphDataList as paragraph]
                                            <w:p w:rsidR="00146544" w:rsidRDefault="00527932">
                                                <w:pPr>
                                                    <w:spacing w:line="480" w:lineRule="exact"/>
                                                    <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                    <w:rPr>
                                                        <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                                                    </w:rPr>
                                                </w:pPr>
                                                [#if paragraph?? && paragraph?size > 0]
                                                    [#list paragraph as sentence]
                                                        [#if sentence?? && sentence.isNonChinese??]
                                                            [#if sentence.isNonChinese == 1]
                                                                <w:r>
                                                                    <w:rPr>
                                                                        <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                                                    </w:rPr>
                                                                    <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                </w:r>
                                                            [#else ]
                                                                <w:r>
                                                                    <w:rPr>
                                                                        <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋" w:hint="eastAsia"/>
                                                                    </w:rPr>
                                                                    <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                </w:r>
                                                            [/#if]
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                            </w:p>
                                        [/#list]
                                    [/#if]
                                [/#if]
                            [/#if]
                        [/#if]
                    [/#if]
                    <w:p w:rsidR="00976D72" w:rsidRDefault="00976D72" w:rsidP="00976D72">
                        <w:pPr>
                            <w:pBdr>
                                <w:bottom w:val="single" w:sz="6" w:space="1" w:color="auto"/>
                            </w:pBdr>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w:rsidR="00976D72" w:rsidRDefault="00976D72" w:rsidP="00976D72">
                        <w:pPr>
                            <w:widowControl/>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:ind w:right="210"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w:rsidR="00976D72" w:rsidRDefault="00976D72" w:rsidP="00976D72">
                        <w:pPr>
                            <w:widowControl/>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:ind w:right="210"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w:rsidR="00976D72" w:rsidRPr="007B7EAB" w:rsidRDefault="00976D72" w:rsidP="00976D72">
                        <w:pPr>
                            <w:widowControl/>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:ind w:right="210"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="007B7EAB">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>教师签名：</w:t>
                        </w:r>
                        <w:r w:rsidRPr="007B7EAB">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                                <w:u w:val="single"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidRPr="00F54555">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>${data.educlassCourseInfo.teacherName}</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve">        </w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                                <w:u w:val="single"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidRPr="007B7EAB">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidRPr="007B7EAB">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>课程负责人签名：</w:t>
                        </w:r>
                        <w:r w:rsidRPr="007B7EAB">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                                <w:u w:val="single"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidRPr="00F54555">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>[#if data.educlassCourseInfo.teamLeader??]${data.educlassCourseInfo.teamLeader}[/#if]</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve">             </w:t>
                        </w:r>

                        <w:r w:rsidRPr="007B7EAB">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                                <w:u w:val="single"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidRPr="007B7EAB">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidRPr="007B7EAB">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>评价日期</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>：</w:t>
                        </w:r>

                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>${data.year}年</w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>${data.month}月</w:t>
                        </w:r>


                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>${data.day}日</w:t>
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="00976D72" w:rsidRPr="007B7EAB" w:rsidRDefault="00976D72" w:rsidP="00976D72">
                        <w:pPr>
                            <w:widowControl/>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:ind w:right="210"/>
                            <w:jc w:val="center"/>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                        </w:pPr>
                        <w:r w:rsidRPr="007B7EAB">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r>
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidRPr="007B7EAB">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t>专业负责人签名：</w:t>
                        </w:r>
                        <w:r w:rsidR="00835551">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                                <w:u w:val="single"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                        <w:r w:rsidR="00835551">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                                <w:u w:val="single"/>
                            </w:rPr>

                        </w:r>
                        <w:r w:rsidRPr="007B7EAB">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                                <w:u w:val="single"/>
                            </w:rPr>
                            <w:t xml:space="preserve">[#if data.educlassCourseInfo.majorName?? ]${data.educlassCourseInfo.majorName}[/#if]</w:t>
                        </w:r>
                        <w:r w:rsidRPr="007B7EAB">
                            <w:rPr>
                                <w:rFonts w:ascii="Times New Roman" w:eastAsia="仿宋" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                <w:bCs/>
                                <w:szCs w:val="21"/>
                            </w:rPr>
                            <w:t xml:space="preserve"></w:t>
                        </w:r>
                    </w:p>
                    <w:p w:rsidR="00976D72" w:rsidRPr="007256F0" w:rsidRDefault="00976D72" w:rsidP="00976D72">
                        <w:pPr>
                            <w:spacing w:line="360" w:lineRule="auto"/>
                            <w:rPr>
                                <w:rFonts w:ascii="仿宋" w:eastAsia="仿宋" w:hAnsi="仿宋"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w:rsidR="00976D72" w:rsidRPr="009C44B7" w:rsidRDefault="00976D72" w:rsidP="00976D72">
                        <w:pPr>
                            <w:rPr>
                                <w:b/>
                                <w:sz w:val="28"/>
                                <w:szCs w:val="28"/>
                            </w:rPr>
                        </w:pPr>
                    </w:p>
                    <w:p w:rsidR="00976D72" w:rsidRDefault="00976D72"/>
                    <w:sectPr w:rsidR="00976D72" w:rsidSect="00F8584D">
                        <w:footerReference w:type="default" r:id="rId7"/>
                        <w:pgSz w:w="11906" w:h="16838"/>
                        <w:pgMar w:top="1361" w:right="1321" w:bottom="919" w:left="1321" w:header="851" w:footer="992" w:gutter="0"/>
                        <w:cols w:space="425"/>
                        <w:docGrid w:type="lines" w:linePitch="312"/>
                    </w:sectPr>
                </w:body>
            </w:document>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/footnotes.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.footnotes+xml">
        <pkg:xmlData>
            <w:footnotes
                    xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
                    xmlns:cx="http://schemas.microsoft.com/office/drawing/2014/chartex"
                    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                    xmlns:o="urn:schemas-microsoft-com:office:office"
                    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                    xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
                    xmlns:v="urn:schemas-microsoft-com:vml"
                    xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
                    xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
                    xmlns:w10="urn:schemas-microsoft-com:office:word"
                    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                    xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                    xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml"
                    xmlns:w16se="http://schemas.microsoft.com/office/word/2015/wordml/symex"
                    xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
                    xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
                    xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
                    xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" mc:Ignorable="w14 w15 w16se wp14">
                <w:footnote w:type="separator" w:id="-1">
                    <w:p w:rsidR="006071C4" w:rsidRDefault="006071C4">
                        <w:r>
                            <w:separator/>
                        </w:r>
                    </w:p>
                </w:footnote>
                <w:footnote w:type="continuationSeparator" w:id="0">
                    <w:p w:rsidR="006071C4" w:rsidRDefault="006071C4">
                        <w:r>
                            <w:continuationSeparator/>
                        </w:r>
                    </w:p>
                </w:footnote>
            </w:footnotes>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/endnotes.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.endnotes+xml">
        <pkg:xmlData>
            <w:endnotes
                    xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
                    xmlns:cx="http://schemas.microsoft.com/office/drawing/2014/chartex"
                    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                    xmlns:o="urn:schemas-microsoft-com:office:office"
                    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                    xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
                    xmlns:v="urn:schemas-microsoft-com:vml"
                    xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
                    xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
                    xmlns:w10="urn:schemas-microsoft-com:office:word"
                    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                    xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                    xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml"
                    xmlns:w16se="http://schemas.microsoft.com/office/word/2015/wordml/symex"
                    xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
                    xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
                    xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
                    xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" mc:Ignorable="w14 w15 w16se wp14">
                <w:endnote w:type="separator" w:id="-1">
                    <w:p w:rsidR="006071C4" w:rsidRDefault="006071C4">
                        <w:r>
                            <w:separator/>
                        </w:r>
                    </w:p>
                </w:endnote>
                <w:endnote w:type="continuationSeparator" w:id="0">
                    <w:p w:rsidR="006071C4" w:rsidRDefault="006071C4">
                        <w:r>
                            <w:continuationSeparator/>
                        </w:r>
                    </w:p>
                </w:endnote>
            </w:endnotes>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/footer1.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.footer+xml">
        <pkg:xmlData>
            <w:ftr
                    xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
                    xmlns:cx="http://schemas.microsoft.com/office/drawing/2014/chartex"
                    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                    xmlns:o="urn:schemas-microsoft-com:office:office"
                    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                    xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
                    xmlns:v="urn:schemas-microsoft-com:vml"
                    xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
                    xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
                    xmlns:w10="urn:schemas-microsoft-com:office:word"
                    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                    xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                    xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml"
                    xmlns:w16se="http://schemas.microsoft.com/office/word/2015/wordml/symex"
                    xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
                    xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
                    xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
                    xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" mc:Ignorable="w14 w15 w16se wp14">
                <w:p w:rsidR="009128C0" w:rsidRDefault="009128C0">
                    <w:pPr>
                        <w:pStyle w:val="a3"/>
                        <w:spacing w:line="14" w:lineRule="auto"/>
                        <w:rPr>
                            <w:sz w:val="20"/>
                        </w:rPr>
                    </w:pPr>
                    <w:r>
                        <w:rPr>
                            <w:noProof/>
                            <w:lang w:val="en-US" w:bidi="ar-SA"/>
                        </w:rPr>
                        <mc:AlternateContent>
                            <mc:Choice Requires="wps">
                                <w:drawing>
                                    <wp:anchor distT="0" distB="0" distL="114300" distR="114300" simplePos="0" relativeHeight="251659264" behindDoc="1" locked="0" layoutInCell="1" allowOverlap="1">
                                        <wp:simplePos x="0" y="0"/>
                                        <wp:positionH relativeFrom="page">
                                            <wp:posOffset>3667760</wp:posOffset>
                                        </wp:positionH>
                                        <wp:positionV relativeFrom="page">
                                            <wp:posOffset>10094595</wp:posOffset>
                                        </wp:positionV>
                                        <wp:extent cx="224790" cy="152400"/>
                                        <wp:effectExtent l="0" t="0" r="0" b="0"/>
                                        <wp:wrapNone/>
                                        <wp:docPr id="2" name="文本框 2"/>
                                        <wp:cNvGraphicFramePr>
                                            <a:graphicFrameLocks
                                                    xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"/>
                                        </wp:cNvGraphicFramePr>
                                        <a:graphic
                                                xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main">
                                            <a:graphicData uri="http://schemas.microsoft.com/office/word/2010/wordprocessingShape">
                                                <wps:wsp>
                                                    <wps:cNvSpPr txBox="1">
                                                        <a:spLocks noChangeArrowheads="1"/>
                                                    </wps:cNvSpPr>
                                                    <wps:spPr bwMode="auto">
                                                        <a:xfrm>
                                                            <a:off x="0" y="0"/>
                                                            <a:ext cx="224790" cy="152400"/>
                                                        </a:xfrm>
                                                        <a:prstGeom prst="rect">
                                                            <a:avLst/>
                                                        </a:prstGeom>
                                                        <a:noFill/>
                                                        <a:ln>
                                                            <a:noFill/>
                                                        </a:ln>
                                                        <a:extLst>
                                                            <a:ext uri="{909E8E84-426E-40DD-AFC4-6F175D3DCCD1}">
                                                                <a14:hiddenFill
                                                                        xmlns:a14="http://schemas.microsoft.com/office/drawing/2010/main">
                                                                    <a:solidFill>
                                                                        <a:srgbClr val="FFFFFF"/>
                                                                    </a:solidFill>
                                                                </a14:hiddenFill>
                                                            </a:ext>
                                                            <a:ext uri="{91240B29-F687-4F45-9708-019B960494DF}">
                                                                <a14:hiddenLine
                                                                        xmlns:a14="http://schemas.microsoft.com/office/drawing/2010/main" w="9525">
                                                                    <a:solidFill>
                                                                        <a:srgbClr val="000000"/>
                                                                    </a:solidFill>
                                                                    <a:miter lim="800000"/>
                                                                    <a:headEnd/>
                                                                    <a:tailEnd/>
                                                                </a14:hiddenLine>
                                                            </a:ext>
                                                        </a:extLst>
                                                    </wps:spPr>
                                                    <wps:txbx>
                                                        <w:txbxContent>
                                                            <w:p w:rsidR="009128C0" w:rsidRDefault="009128C0">
                                                                <w:pPr>
                                                                    <w:spacing w:before="12"/>
                                                                    <w:ind w:left="40"/>
                                                                    <w:rPr>
                                                                        <w:rFonts w:ascii="Times New Roman"/>
                                                                        <w:sz w:val="18"/>
                                                                    </w:rPr>
                                                                </w:pPr>
                                                                <w:r>
                                                                    <w:fldChar w:fldCharType="begin"/>
                                                                </w:r>
                                                                <w:r>
                                                                    <w:rPr>
                                                                        <w:rFonts w:ascii="Times New Roman"/>
                                                                        <w:sz w:val="18"/>
                                                                    </w:rPr>
                                                                    <w:instrText xml:space="preserve"> PAGE </w:instrText>
                                                                </w:r>
                                                                <w:r>
                                                                    <w:fldChar w:fldCharType="separate"/>
                                                                </w:r>
                                                                <w:r w:rsidR="006B6EA8">
                                                                    <w:rPr>
                                                                        <w:rFonts w:ascii="Times New Roman"/>
                                                                        <w:noProof/>
                                                                        <w:sz w:val="18"/>
                                                                    </w:rPr>
                                                                    <w:t>4</w:t>
                                                                </w:r>
                                                                <w:r>
                                                                    <w:fldChar w:fldCharType="end"/>
                                                                </w:r>
                                                            </w:p>
                                                        </w:txbxContent>
                                                    </wps:txbx>
                                                    <wps:bodyPr rot="0" vert="horz" wrap="square" lIns="0" tIns="0" rIns="0" bIns="0" anchor="t" anchorCtr="0" upright="1">
                                                        <a:noAutofit/>
                                                    </wps:bodyPr>
                                                </wps:wsp>
                                            </a:graphicData>
                                        </a:graphic>
                                        <wp14:sizeRelH relativeFrom="page">
                                            <wp14:pctWidth>0</wp14:pctWidth>
                                        </wp14:sizeRelH>
                                        <wp14:sizeRelV relativeFrom="page">
                                            <wp14:pctHeight>0</wp14:pctHeight>
                                        </wp14:sizeRelV>
                                    </wp:anchor>
                                </w:drawing>
                            </mc:Choice>
                            <mc:Fallback>
                                <w:pict>
                                    <v:shapetype id="_x0000_t202" coordsize="21600,21600" o:spt="202" path="m,l,21600r21600,l21600,xe">
                                        <v:stroke joinstyle="miter"/>
                                        <v:path gradientshapeok="t" o:connecttype="rect"/>
                                    </v:shapetype>
                                    <v:shape id="文本框 2" o:spid="_x0000_s1026" type="#_x0000_t202" style="position:absolute;margin-left:288.8pt;margin-top:794.85pt;width:17.7pt;height:12pt;z-index:-251657216;visibility:visible;mso-wrap-style:square;mso-width-percent:0;mso-height-percent:0;mso-wrap-distance-left:9pt;mso-wrap-distance-top:0;mso-wrap-distance-right:9pt;mso-wrap-distance-bottom:0;mso-position-horizontal:absolute;mso-position-horizontal-relative:page;mso-position-vertical:absolute;mso-position-vertical-relative:page;mso-width-percent:0;mso-height-percent:0;mso-width-relative:page;mso-height-relative:page;v-text-anchor:top" o:gfxdata="UEsDBBQABgAIAAAAIQC2gziS/gAAAOEBAAATAAAAW0NvbnRlbnRfVHlwZXNdLnhtbJSRQU7DMBBF&#xA;90jcwfIWJU67QAgl6YK0S0CoHGBkTxKLZGx5TGhvj5O2G0SRWNoz/78nu9wcxkFMGNg6quQqL6RA&#xA;0s5Y6ir5vt9lD1JwBDIwOMJKHpHlpr69KfdHjyxSmriSfYz+USnWPY7AufNIadK6MEJMx9ApD/oD&#xA;OlTrorhX2lFEilmcO2RdNtjC5xDF9pCuTyYBB5bi6bQ4syoJ3g9WQ0ymaiLzg5KdCXlKLjvcW893&#xA;SUOqXwnz5DrgnHtJTxOsQfEKIT7DmDSUCaxw7Rqn8787ZsmRM9e2VmPeBN4uqYvTtW7jvijg9N/y&#xA;JsXecLq0q+WD6m8AAAD//wMAUEsDBBQABgAIAAAAIQA4/SH/1gAAAJQBAAALAAAAX3JlbHMvLnJl&#xA;bHOkkMFqwzAMhu+DvYPRfXGawxijTi+j0GvpHsDYimMaW0Yy2fr2M4PBMnrbUb/Q94l/f/hMi1qR&#xA;JVI2sOt6UJgd+ZiDgffL8ekFlFSbvV0oo4EbChzGx4f9GRdb25HMsYhqlCwG5lrLq9biZkxWOiqY&#xA;22YiTra2kYMu1l1tQD30/bPm3wwYN0x18gb45AdQl1tp5j/sFB2T0FQ7R0nTNEV3j6o9feQzro1i&#xA;OWA14Fm+Q8a1a8+Bvu/d/dMb2JY5uiPbhG/ktn4cqGU/er3pcvwCAAD//wMAUEsDBBQABgAIAAAA&#xA;IQDnmMbivQIAAKkFAAAOAAAAZHJzL2Uyb0RvYy54bWysVEuO1DAQ3SNxB8v7TD6kP4kmPZrpdBDS&#xA;8JEGDuBOnI5FYgfb3ckwYgs3YMWGPefqc1B2Ot3z2SAgC6til1/Vq3qu84u+qdGOSsUET7B/5mFE&#xA;eS4KxjcJ/vA+c+YYKU14QWrBaYJvqcIXi+fPzrs2poGoRF1QiQCEq7hrE1xp3cauq/KKNkSdiZZy&#xA;OCyFbIiGX7lxC0k6QG9qN/C8qdsJWbRS5FQp2E2HQ7yw+GVJc/22LBXVqE4w5KbtKu26Nqu7OCfx&#xA;RpK2YvkhDfIXWTSEcQh6hEqJJmgr2ROohuVSKFHqs1w0rihLllPLAdj43iM2NxVpqeUCxVHtsUzq&#xA;/8Hmb3bvJGJFggOMOGmgRfvv3/Y/fu1/fkWBKU/Xqhi8blrw0/2V6KHNlqpqr0X+USEulhXhG3op&#xA;pegqSgpIzzc33XtXBxxlQNbda1FAHLLVwgL1pWxM7aAaCNChTbfH1tBeoxw2gyCcRXCSw5E/CULP&#xA;ts4l8Xi5lUq/pKJBxkiwhM5bcLK7VtokQ+LRxcTiImN1bbtf8wcb4DjsQGi4as5MEraZd5EXrear&#xA;eeiEwXTlhF6aOpfZMnSmmT+bpC/S5TL1v5i4fhhXrCgoN2FGYfnhnzXuIPFBEkdpKVGzwsCZlJTc&#xA;rJe1RDsCws7sZ0sOJyc392EatgjA5RElH6p5FURONp3PnDALJ0408+aO50dX0dQLozDNHlK6Zpz+&#xA;OyXUJTiaBJNBS6ekH3Hz7PeUG4kbpmF01KxJ8PzoRGKjwBUvbGs1YfVg3yuFSf9UCmj32GirVyPR&#xA;Qay6X/eAYkS8FsUtKFcKUBaIEOYdGJWQnzHqYHYkWH3aEkkxql9xUL8ZNKMhR2M9GoTncDXBGqPB&#xA;XOphIG1byTYVIA/vi4tLeCEls+o9ZXF4VzAPLInD7DID5/6/9TpN2MVvAAAA//8DAFBLAwQUAAYA&#xA;CAAAACEAUrdJgeEAAAANAQAADwAAAGRycy9kb3ducmV2LnhtbEyPQU/DMAyF70j8h8hI3Fg60Nqt&#xA;NJ0mBCckRFcOHNPGa6M1Tmmyrfx7zAlutt/T8/eK7ewGccYpWE8KlosEBFLrjaVOwUf9crcGEaIm&#xA;owdPqOAbA2zL66tC58ZfqMLzPnaCQyjkWkEf45hLGdoenQ4LPyKxdvCT05HXqZNm0hcOd4O8T5JU&#xA;Om2JP/R6xKce2+P+5BTsPql6tl9vzXt1qGxdbxJ6TY9K3d7Mu0cQEef4Z4ZffEaHkpkafyITxKBg&#xA;lWUpW1lYrTcZCLakyweu1/CJpwxkWcj/LcofAAAA//8DAFBLAQItABQABgAIAAAAIQC2gziS/gAA&#xA;AOEBAAATAAAAAAAAAAAAAAAAAAAAAABbQ29udGVudF9UeXBlc10ueG1sUEsBAi0AFAAGAAgAAAAh&#xA;ADj9If/WAAAAlAEAAAsAAAAAAAAAAAAAAAAALwEAAF9yZWxzLy5yZWxzUEsBAi0AFAAGAAgAAAAh&#xA;AOeYxuK9AgAAqQUAAA4AAAAAAAAAAAAAAAAALgIAAGRycy9lMm9Eb2MueG1sUEsBAi0AFAAGAAgA&#xA;AAAhAFK3SYHhAAAADQEAAA8AAAAAAAAAAAAAAAAAFwUAAGRycy9kb3ducmV2LnhtbFBLBQYAAAAA&#xA;BAAEAPMAAAAlBgAAAAA=&#xA;" filled="f" stroked="f">
                                        <v:textbox inset="0,0,0,0">
                                            <w:txbxContent>
                                                <w:p w:rsidR="009128C0" w:rsidRDefault="009128C0">
                                                    <w:pPr>
                                                        <w:spacing w:before="12"/>
                                                        <w:ind w:left="40"/>
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="Times New Roman"/>
                                                            <w:sz w:val="18"/>
                                                        </w:rPr>
                                                    </w:pPr>
                                                    <w:r>
                                                        <w:fldChar w:fldCharType="begin"/>
                                                    </w:r>
                                                    <w:r>
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="Times New Roman"/>
                                                            <w:sz w:val="18"/>
                                                        </w:rPr>
                                                        <w:instrText xml:space="preserve"> PAGE </w:instrText>
                                                    </w:r>
                                                    <w:r>
                                                        <w:fldChar w:fldCharType="separate"/>
                                                    </w:r>
                                                    <w:r w:rsidR="006B6EA8">
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="Times New Roman"/>
                                                            <w:noProof/>
                                                            <w:sz w:val="18"/>
                                                        </w:rPr>
                                                        <w:t>4</w:t>
                                                    </w:r>
                                                    <w:r>
                                                        <w:fldChar w:fldCharType="end"/>
                                                    </w:r>
                                                </w:p>
                                            </w:txbxContent>
                                        </v:textbox>
                                        <w10:wrap anchorx="page" anchory="page"/>
                                    </v:shape>
                                </w:pict>
                            </mc:Fallback>
                        </mc:AlternateContent>
                    </w:r>
                </w:p>
            </w:ftr>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/theme/theme1.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.theme+xml">
        <pkg:xmlData>
            <a:theme
                    xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" name="Office 主题">
                <a:themeElements>
                    <a:clrScheme name="Office">
                        <a:dk1>
                            <a:sysClr val="windowText" lastClr="000000"/>
                        </a:dk1>
                        <a:lt1>
                            <a:sysClr val="window" lastClr="FFFFFF"/>
                        </a:lt1>
                        <a:dk2>
                            <a:srgbClr val="44546A"/>
                        </a:dk2>
                        <a:lt2>
                            <a:srgbClr val="E7E6E6"/>
                        </a:lt2>
                        <a:accent1>
                            <a:srgbClr val="5B9BD5"/>
                        </a:accent1>
                        <a:accent2>
                            <a:srgbClr val="ED7D31"/>
                        </a:accent2>
                        <a:accent3>
                            <a:srgbClr val="A5A5A5"/>
                        </a:accent3>
                        <a:accent4>
                            <a:srgbClr val="FFC000"/>
                        </a:accent4>
                        <a:accent5>
                            <a:srgbClr val="4472C4"/>
                        </a:accent5>
                        <a:accent6>
                            <a:srgbClr val="70AD47"/>
                        </a:accent6>
                        <a:hlink>
                            <a:srgbClr val="0563C1"/>
                        </a:hlink>
                        <a:folHlink>
                            <a:srgbClr val="954F72"/>
                        </a:folHlink>
                    </a:clrScheme>
                    <a:fontScheme name="Office">
                        <a:majorFont>
                            <a:latin typeface="Calibri Light" panose="020F0302020204030204"/>
                            <a:ea typeface=""/>
                            <a:cs typeface=""/>
                            <a:font script="Jpan" typeface="ＭＳ ゴシック"/>
                            <a:font script="Hang" typeface="맑은 고딕"/>
                            <a:font script="Hans" typeface="宋体"/>
                            <a:font script="Hant" typeface="新細明體"/>
                            <a:font script="Arab" typeface="Times New Roman"/>
                            <a:font script="Hebr" typeface="Times New Roman"/>
                            <a:font script="Thai" typeface="Angsana New"/>
                            <a:font script="Ethi" typeface="Nyala"/>
                            <a:font script="Beng" typeface="Vrinda"/>
                            <a:font script="Gujr" typeface="Shruti"/>
                            <a:font script="Khmr" typeface="MoolBoran"/>
                            <a:font script="Knda" typeface="Tunga"/>
                            <a:font script="Guru" typeface="Raavi"/>
                            <a:font script="Cans" typeface="Euphemia"/>
                            <a:font script="Cher" typeface="Plantagenet Cherokee"/>
                            <a:font script="Yiii" typeface="Microsoft Yi Baiti"/>
                            <a:font script="Tibt" typeface="Microsoft Himalaya"/>
                            <a:font script="Thaa" typeface="MV Boli"/>
                            <a:font script="Deva" typeface="Mangal"/>
                            <a:font script="Telu" typeface="Gautami"/>
                            <a:font script="Taml" typeface="Latha"/>
                            <a:font script="Syrc" typeface="Estrangelo Edessa"/>
                            <a:font script="Orya" typeface="Kalinga"/>
                            <a:font script="Mlym" typeface="Kartika"/>
                            <a:font script="Laoo" typeface="DokChampa"/>
                            <a:font script="Sinh" typeface="Iskoola Pota"/>
                            <a:font script="Mong" typeface="Mongolian Baiti"/>
                            <a:font script="Viet" typeface="Times New Roman"/>
                            <a:font script="Uigh" typeface="Microsoft Uighur"/>
                            <a:font script="Geor" typeface="Sylfaen"/>
                        </a:majorFont>
                        <a:minorFont>
                            <a:latin typeface="Calibri" panose="020F0502020204030204"/>
                            <a:ea typeface=""/>
                            <a:cs typeface=""/>
                            <a:font script="Jpan" typeface="ＭＳ 明朝"/>
                            <a:font script="Hang" typeface="맑은 고딕"/>
                            <a:font script="Hans" typeface="宋体"/>
                            <a:font script="Hant" typeface="新細明體"/>
                            <a:font script="Arab" typeface="Arial"/>
                            <a:font script="Hebr" typeface="Arial"/>
                            <a:font script="Thai" typeface="Cordia New"/>
                            <a:font script="Ethi" typeface="Nyala"/>
                            <a:font script="Beng" typeface="Vrinda"/>
                            <a:font script="Gujr" typeface="Shruti"/>
                            <a:font script="Khmr" typeface="DaunPenh"/>
                            <a:font script="Knda" typeface="Tunga"/>
                            <a:font script="Guru" typeface="Raavi"/>
                            <a:font script="Cans" typeface="Euphemia"/>
                            <a:font script="Cher" typeface="Plantagenet Cherokee"/>
                            <a:font script="Yiii" typeface="Microsoft Yi Baiti"/>
                            <a:font script="Tibt" typeface="Microsoft Himalaya"/>
                            <a:font script="Thaa" typeface="MV Boli"/>
                            <a:font script="Deva" typeface="Mangal"/>
                            <a:font script="Telu" typeface="Gautami"/>
                            <a:font script="Taml" typeface="Latha"/>
                            <a:font script="Syrc" typeface="Estrangelo Edessa"/>
                            <a:font script="Orya" typeface="Kalinga"/>
                            <a:font script="Mlym" typeface="Kartika"/>
                            <a:font script="Laoo" typeface="DokChampa"/>
                            <a:font script="Sinh" typeface="Iskoola Pota"/>
                            <a:font script="Mong" typeface="Mongolian Baiti"/>
                            <a:font script="Viet" typeface="Arial"/>
                            <a:font script="Uigh" typeface="Microsoft Uighur"/>
                            <a:font script="Geor" typeface="Sylfaen"/>
                        </a:minorFont>
                    </a:fontScheme>
                    <a:fmtScheme name="Office">
                        <a:fillStyleLst>
                            <a:solidFill>
                                <a:schemeClr val="phClr"/>
                            </a:solidFill>
                            <a:gradFill rotWithShape="1">
                                <a:gsLst>
                                    <a:gs pos="0">
                                        <a:schemeClr val="phClr">
                                            <a:lumMod val="110000"/>
                                            <a:satMod val="105000"/>
                                            <a:tint val="67000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="50000">
                                        <a:schemeClr val="phClr">
                                            <a:lumMod val="105000"/>
                                            <a:satMod val="103000"/>
                                            <a:tint val="73000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:lumMod val="105000"/>
                                            <a:satMod val="109000"/>
                                            <a:tint val="81000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:lin ang="5400000" scaled="0"/>
                            </a:gradFill>
                            <a:gradFill rotWithShape="1">
                                <a:gsLst>
                                    <a:gs pos="0">
                                        <a:schemeClr val="phClr">
                                            <a:satMod val="103000"/>
                                            <a:lumMod val="102000"/>
                                            <a:tint val="94000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="50000">
                                        <a:schemeClr val="phClr">
                                            <a:satMod val="110000"/>
                                            <a:lumMod val="100000"/>
                                            <a:shade val="100000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:lumMod val="99000"/>
                                            <a:satMod val="120000"/>
                                            <a:shade val="78000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:lin ang="5400000" scaled="0"/>
                            </a:gradFill>
                        </a:fillStyleLst>
                        <a:lnStyleLst>
                            <a:ln w="6350" cap="flat" cmpd="sng" algn="ctr">
                                <a:solidFill>
                                    <a:schemeClr val="phClr"/>
                                </a:solidFill>
                                <a:prstDash val="solid"/>
                                <a:miter lim="800000"/>
                            </a:ln>
                            <a:ln w="12700" cap="flat" cmpd="sng" algn="ctr">
                                <a:solidFill>
                                    <a:schemeClr val="phClr"/>
                                </a:solidFill>
                                <a:prstDash val="solid"/>
                                <a:miter lim="800000"/>
                            </a:ln>
                            <a:ln w="19050" cap="flat" cmpd="sng" algn="ctr">
                                <a:solidFill>
                                    <a:schemeClr val="phClr"/>
                                </a:solidFill>
                                <a:prstDash val="solid"/>
                                <a:miter lim="800000"/>
                            </a:ln>
                        </a:lnStyleLst>
                        <a:effectStyleLst>
                            <a:effectStyle>
                                <a:effectLst/>
                            </a:effectStyle>
                            <a:effectStyle>
                                <a:effectLst/>
                            </a:effectStyle>
                            <a:effectStyle>
                                <a:effectLst>
                                    <a:outerShdw blurRad="57150" dist="19050" dir="5400000" algn="ctr" rotWithShape="0">
                                        <a:srgbClr val="000000">
                                            <a:alpha val="63000"/>
                                        </a:srgbClr>
                                    </a:outerShdw>
                                </a:effectLst>
                            </a:effectStyle>
                        </a:effectStyleLst>
                        <a:bgFillStyleLst>
                            <a:solidFill>
                                <a:schemeClr val="phClr"/>
                            </a:solidFill>
                            <a:solidFill>
                                <a:schemeClr val="phClr">
                                    <a:tint val="95000"/>
                                    <a:satMod val="170000"/>
                                </a:schemeClr>
                            </a:solidFill>
                            <a:gradFill rotWithShape="1">
                                <a:gsLst>
                                    <a:gs pos="0">
                                        <a:schemeClr val="phClr">
                                            <a:tint val="93000"/>
                                            <a:satMod val="150000"/>
                                            <a:shade val="98000"/>
                                            <a:lumMod val="102000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="50000">
                                        <a:schemeClr val="phClr">
                                            <a:tint val="98000"/>
                                            <a:satMod val="130000"/>
                                            <a:shade val="90000"/>
                                            <a:lumMod val="103000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                    <a:gs pos="100000">
                                        <a:schemeClr val="phClr">
                                            <a:shade val="63000"/>
                                            <a:satMod val="120000"/>
                                        </a:schemeClr>
                                    </a:gs>
                                </a:gsLst>
                                <a:lin ang="5400000" scaled="0"/>
                            </a:gradFill>
                        </a:bgFillStyleLst>
                    </a:fmtScheme>
                </a:themeElements>
                <a:objectDefaults/>
                <a:extraClrSchemeLst/>
                <a:extLst>
                    <a:ext uri="{05A4C25C-085E-4340-85A3-A5531E510DB2}">
                        <thm15:themeFamily
                                xmlns:thm15="http://schemas.microsoft.com/office/thememl/2012/main" name="Office Theme" id="{62F939B6-93AF-4DB8-9C6B-D6C7DFDC589F}" vid="{4A3C46E8-61CC-4603-A589-7422A47A8E4A}"/>
                    </a:ext>
                </a:extLst>
            </a:theme>
        </pkg:xmlData>
    </pkg:part>
    [#--图片的B64编码--]
    [#if data.imageList??]
        [#list data.imageList as imageList]
            <pkg:part pkg:name="/word/${imageList.imageNameNo}" pkg:contentType="image/jpeg" pkg:compression="store">
                <pkg:binaryData>${imageList.imageB64}</pkg:binaryData>
            </pkg:part>
        [/#list]
    [/#if]

    <pkg:part pkg:name="/word/settings.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml">
        <pkg:xmlData>
            <w:settings
                    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                    xmlns:o="urn:schemas-microsoft-com:office:office"
                    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                    xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
                    xmlns:v="urn:schemas-microsoft-com:vml"
                    xmlns:w10="urn:schemas-microsoft-com:office:word"
                    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                    xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                    xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml"
                    xmlns:w16se="http://schemas.microsoft.com/office/word/2015/wordml/symex"
                    xmlns:sl="http://schemas.openxmlformats.org/schemaLibrary/2006/main" mc:Ignorable="w14 w15 w16se">
                <w:zoom w:percent="100"/>
                <w:bordersDoNotSurroundHeader/>
                <w:bordersDoNotSurroundFooter/>
                <w:defaultTabStop w:val="420"/>
                <w:drawingGridVerticalSpacing w:val="156"/>
                <w:displayHorizontalDrawingGridEvery w:val="0"/>
                <w:displayVerticalDrawingGridEvery w:val="2"/>
                <w:characterSpacingControl w:val="compressPunctuation"/>
                <w:hdrShapeDefaults>
                    <o:shapedefaults v:ext="edit" spidmax="2049"/>
                </w:hdrShapeDefaults>
                <w:footnotePr>
                    <w:footnote w:id="-1"/>
                    <w:footnote w:id="0"/>
                </w:footnotePr>
                <w:endnotePr>
                    <w:endnote w:id="-1"/>
                    <w:endnote w:id="0"/>
                </w:endnotePr>
                <w:compat>
                    <w:spaceForUL/>
                    <w:balanceSingleByteDoubleByteWidth/>
                    <w:doNotLeaveBackslashAlone/>
                    <w:ulTrailSpace/>
                    <w:doNotExpandShiftReturn/>
                    <w:adjustLineHeightInTable/>
                    <w:useFELayout/>
                    <w:compatSetting w:name="compatibilityMode" w:uri="http://schemas.microsoft.com/office/word" w:val="15"/>
                    <w:compatSetting w:name="overrideTableStyleFontSizeAndJustification" w:uri="http://schemas.microsoft.com/office/word" w:val="1"/>
                    <w:compatSetting w:name="enableOpenTypeFeatures" w:uri="http://schemas.microsoft.com/office/word" w:val="1"/>
                    <w:compatSetting w:name="doNotFlipMirrorIndents" w:uri="http://schemas.microsoft.com/office/word" w:val="1"/>
                    <w:compatSetting w:name="differentiateMultirowTableHeaders" w:uri="http://schemas.microsoft.com/office/word" w:val="1"/>
                </w:compat>
                <w:rsids>
                    <w:rsidRoot w:val="00C53283"/>
                    <w:rsid w:val="00076F8A"/>
                    <w:rsid w:val="000C2474"/>
                    <w:rsid w:val="000C6F0A"/>
                    <w:rsid w:val="00157490"/>
                    <w:rsid w:val="00162820"/>
                    <w:rsid w:val="001B0CAC"/>
                    <w:rsid w:val="001E209E"/>
                    <w:rsid w:val="001F0F7E"/>
                    <w:rsid w:val="00260610"/>
                    <w:rsid w:val="002B3062"/>
                    <w:rsid w:val="00311CD1"/>
                    <w:rsid w:val="00333C69"/>
                    <w:rsid w:val="003A62D2"/>
                    <w:rsid w:val="003B1D70"/>
                    <w:rsid w:val="00463A09"/>
                    <w:rsid w:val="00487263"/>
                    <w:rsid w:val="00487D50"/>
                    <w:rsid w:val="0051427C"/>
                    <w:rsid w:val="00514BB4"/>
                    <w:rsid w:val="0053123F"/>
                    <w:rsid w:val="005D2C56"/>
                    <w:rsid w:val="006071C4"/>
                    <w:rsid w:val="00660E30"/>
                    <w:rsid w:val="00670501"/>
                    <w:rsid w:val="00670F9A"/>
                    <w:rsid w:val="00687F7F"/>
                    <w:rsid w:val="006B6EA8"/>
                    <w:rsid w:val="006B6EDE"/>
                    <w:rsid w:val="006D1126"/>
                    <w:rsid w:val="00741DEA"/>
                    <w:rsid w:val="0077477D"/>
                    <w:rsid w:val="00835551"/>
                    <w:rsid w:val="00874A11"/>
                    <w:rsid w:val="008B7772"/>
                    <w:rsid w:val="009128C0"/>
                    <w:rsid w:val="00976D72"/>
                    <w:rsid w:val="0098205D"/>
                    <w:rsid w:val="009B0BE5"/>
                    <w:rsid w:val="009C4F63"/>
                    <w:rsid w:val="009C5679"/>
                    <w:rsid w:val="009D51B8"/>
                    <w:rsid w:val="00A018B4"/>
                    <w:rsid w:val="00A10BEE"/>
                    <w:rsid w:val="00A55AD9"/>
                    <w:rsid w:val="00A93330"/>
                    <w:rsid w:val="00AD54D6"/>
                    <w:rsid w:val="00AD5D96"/>
                    <w:rsid w:val="00B158A0"/>
                    <w:rsid w:val="00B2075B"/>
                    <w:rsid w:val="00B21C11"/>
                    <w:rsid w:val="00B3230C"/>
                    <w:rsid w:val="00B47E44"/>
                    <w:rsid w:val="00BD26DB"/>
                    <w:rsid w:val="00BD4A3D"/>
                    <w:rsid w:val="00C37FF6"/>
                    <w:rsid w:val="00C53283"/>
                    <w:rsid w:val="00C655F7"/>
                    <w:rsid w:val="00CC5CB0"/>
                    <w:rsid w:val="00CE7C97"/>
                    <w:rsid w:val="00CF64C4"/>
                    <w:rsid w:val="00D15CDD"/>
                    <w:rsid w:val="00D27678"/>
                    <w:rsid w:val="00D27CD1"/>
                    <w:rsid w:val="00D3301E"/>
                    <w:rsid w:val="00D91B6F"/>
                    <w:rsid w:val="00DC7C73"/>
                    <w:rsid w:val="00E11A30"/>
                    <w:rsid w:val="00E32E88"/>
                    <w:rsid w:val="00EC7E65"/>
                    <w:rsid w:val="00ED1264"/>
                    <w:rsid w:val="00F464C3"/>
                    <w:rsid w:val="00F8584D"/>
                    <w:rsid w:val="00FF342B"/>
                </w:rsids>
                <m:mathPr>
                    <m:mathFont m:val="Cambria Math"/>
                    <m:brkBin m:val="before"/>
                    <m:brkBinSub m:val="--"/>
                    <m:smallFrac m:val="0"/>
                    <m:dispDef/>
                    <m:lMargin m:val="0"/>
                    <m:rMargin m:val="0"/>
                    <m:defJc m:val="centerGroup"/>
                    <m:wrapIndent m:val="1440"/>
                    <m:intLim m:val="subSup"/>
                    <m:naryLim m:val="undOvr"/>
                </m:mathPr>
                <w:themeFontLang w:val="en-US" w:eastAsia="zh-CN"/>
                <w:clrSchemeMapping w:bg1="light1" w:t1="dark1" w:bg2="light2" w:t2="dark2" w:accent1="accent1" w:accent2="accent2" w:accent3="accent3" w:accent4="accent4" w:accent5="accent5" w:accent6="accent6" w:hyperlink="hyperlink" w:followedHyperlink="followedHyperlink"/>
                <w:shapeDefaults>
                    <o:shapedefaults v:ext="edit" spidmax="2049"/>
                    <o:shapelayout v:ext="edit">
                        <o:idmap v:ext="edit" data="1"/>
                    </o:shapelayout>
                </w:shapeDefaults>
                <w:decimalSymbol w:val="."/>
                <w:listSeparator w:val=","/>
                <w14:docId w14:val="0A5D039F"/>
                <w15:chartTrackingRefBased/>
                <w15:docId w15:val="{DF455316-71E2-4EB7-A2E7-ADA8FE4BEBF4}"/>
            </w:settings>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/webSettings.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.webSettings+xml">
        <pkg:xmlData>
            <w:webSettings
                    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                    xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                    xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml"
                    xmlns:w16se="http://schemas.microsoft.com/office/word/2015/wordml/symex" mc:Ignorable="w14 w15 w16se">
                <w:divs>
                    <w:div w:id="1328708373">
                        <w:bodyDiv w:val="1"/>
                        <w:marLeft w:val="0"/>
                        <w:marRight w:val="0"/>
                        <w:marTop w:val="0"/>
                        <w:marBottom w:val="0"/>
                        <w:divBdr>
                            <w:top w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:left w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:bottom w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:right w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                        </w:divBdr>
                    </w:div>
                    <w:div w:id="1443956601">
                        <w:bodyDiv w:val="1"/>
                        <w:marLeft w:val="0"/>
                        <w:marRight w:val="0"/>
                        <w:marTop w:val="0"/>
                        <w:marBottom w:val="0"/>
                        <w:divBdr>
                            <w:top w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:left w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:bottom w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:right w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                        </w:divBdr>
                    </w:div>
                    <w:div w:id="1556047113">
                        <w:bodyDiv w:val="1"/>
                        <w:marLeft w:val="0"/>
                        <w:marRight w:val="0"/>
                        <w:marTop w:val="0"/>
                        <w:marBottom w:val="0"/>
                        <w:divBdr>
                            <w:top w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:left w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:bottom w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                            <w:right w:val="none" w:sz="0" w:space="0" w:color="auto"/>
                        </w:divBdr>
                    </w:div>
                </w:divs>
                <w:optimizeForBrowser/>
                <w:allowPNG/>
            </w:webSettings>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/styles.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml">
        <pkg:xmlData>
            <w:styles
                    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                    xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                    xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml"
                    xmlns:w16se="http://schemas.microsoft.com/office/word/2015/wordml/symex" mc:Ignorable="w14 w15 w16se">
                <w:docDefaults>
                    <w:rPrDefault>
                        <w:rPr>
                            <w:rFonts w:asciiTheme="minorHAnsi" w:eastAsiaTheme="minorEastAsia" w:hAnsiTheme="minorHAnsi" w:cstheme="minorBidi"/>
                            <w:kern w:val="2"/>
                            <w:sz w:val="21"/>
                            <w:szCs w:val="22"/>
                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                        </w:rPr>
                    </w:rPrDefault>
                    <w:pPrDefault/>
                </w:docDefaults>
                <w:latentStyles w:defLockedState="0" w:defUIPriority="99" w:defSemiHidden="0" w:defUnhideWhenUsed="0" w:defQFormat="0" w:count="371">
                    <w:lsdException w:name="Normal" w:uiPriority="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 1" w:uiPriority="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 2" w:semiHidden="1" w:uiPriority="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 3" w:semiHidden="1" w:uiPriority="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 4" w:semiHidden="1" w:uiPriority="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 5" w:semiHidden="1" w:uiPriority="9" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 6" w:semiHidden="1" w:uiPriority="9" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 7" w:semiHidden="1" w:uiPriority="9" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 8" w:semiHidden="1" w:uiPriority="9" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 9" w:semiHidden="1" w:uiPriority="9" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="index 1" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="index 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="index 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="index 4" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="index 5" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="index 6" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="index 7" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="index 8" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="index 9" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="toc 1" w:semiHidden="1" w:uiPriority="39" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="toc 2" w:semiHidden="1" w:uiPriority="39" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="toc 3" w:semiHidden="1" w:uiPriority="39" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="toc 4" w:semiHidden="1" w:uiPriority="39" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="toc 5" w:semiHidden="1" w:uiPriority="39" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="toc 6" w:semiHidden="1" w:uiPriority="39" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="toc 7" w:semiHidden="1" w:uiPriority="39" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="toc 8" w:semiHidden="1" w:uiPriority="39" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="toc 9" w:semiHidden="1" w:uiPriority="39" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Normal Indent" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="footnote text" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="annotation text" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="header" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="footer" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="index heading" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="caption" w:semiHidden="1" w:uiPriority="35" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="table of figures" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="envelope address" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="envelope return" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="footnote reference" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="annotation reference" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="line number" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="page number" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="endnote reference" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="endnote text" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="table of authorities" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="macro" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="toa heading" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Bullet" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Number" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List 4" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List 5" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Bullet 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Bullet 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Bullet 4" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Bullet 5" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Number 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Number 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Number 4" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Number 5" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Title" w:uiPriority="10" w:qFormat="1"/>
                    <w:lsdException w:name="Closing" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Signature" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Default Paragraph Font" w:semiHidden="1" w:uiPriority="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Body Text" w:semiHidden="1" w:uiPriority="1" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="Body Text Indent" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Continue" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Continue 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Continue 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Continue 4" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="List Continue 5" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Message Header" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Subtitle" w:uiPriority="11" w:qFormat="1"/>
                    <w:lsdException w:name="Salutation" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Date" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Body Text First Indent" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Body Text First Indent 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Note Heading" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Body Text 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Body Text 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Body Text Indent 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Body Text Indent 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Block Text" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Hyperlink" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="FollowedHyperlink" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Strong" w:uiPriority="22" w:qFormat="1"/>
                    <w:lsdException w:name="Emphasis" w:uiPriority="20" w:qFormat="1"/>
                    <w:lsdException w:name="Document Map" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Plain Text" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="E-mail Signature" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="HTML Top of Form" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="HTML Bottom of Form" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Normal (Web)" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="HTML Acronym" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="HTML Address" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="HTML Cite" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="HTML Code" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="HTML Definition" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="HTML Keyboard" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="HTML Preformatted" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="HTML Sample" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="HTML Typewriter" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="HTML Variable" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Normal Table" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="annotation subject" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="No List" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Outline List 1" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Outline List 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Outline List 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Simple 1" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Simple 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Simple 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Classic 1" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Classic 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Classic 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Classic 4" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Colorful 1" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Colorful 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Colorful 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Columns 1" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Columns 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Columns 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Columns 4" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Columns 5" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Grid 1" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Grid 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Grid 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Grid 4" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Grid 5" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Grid 6" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Grid 7" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Grid 8" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table List 1" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table List 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table List 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table List 4" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table List 5" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table List 6" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table List 7" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table List 8" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table 3D effects 1" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table 3D effects 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table 3D effects 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Contemporary" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Elegant" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Professional" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Subtle 1" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Subtle 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Web 1" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Web 2" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Web 3" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Balloon Text" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Table Grid" w:uiPriority="39"/>
                    <w:lsdException w:name="Table Theme" w:semiHidden="1" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="Placeholder Text" w:semiHidden="1"/>
                    <w:lsdException w:name="No Spacing" w:uiPriority="1" w:qFormat="1"/>
                    <w:lsdException w:name="Light Shading" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1" w:uiPriority="65"/>
                    <w:lsdException w:name="Medium List 2" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid" w:uiPriority="73"/>
                    <w:lsdException w:name="Light Shading Accent 1" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List Accent 1" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid Accent 1" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1 Accent 1" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2 Accent 1" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1 Accent 1" w:uiPriority="65"/>
                    <w:lsdException w:name="Revision" w:semiHidden="1"/>
                    <w:lsdException w:name="List Paragraph" w:uiPriority="34" w:qFormat="1"/>
                    <w:lsdException w:name="Quote" w:uiPriority="29" w:qFormat="1"/>
                    <w:lsdException w:name="Intense Quote" w:uiPriority="30" w:qFormat="1"/>
                    <w:lsdException w:name="Medium List 2 Accent 1" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1 Accent 1" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2 Accent 1" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3 Accent 1" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List Accent 1" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading Accent 1" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List Accent 1" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid Accent 1" w:uiPriority="73"/>
                    <w:lsdException w:name="Light Shading Accent 2" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List Accent 2" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid Accent 2" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1 Accent 2" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2 Accent 2" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1 Accent 2" w:uiPriority="65"/>
                    <w:lsdException w:name="Medium List 2 Accent 2" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1 Accent 2" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2 Accent 2" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3 Accent 2" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List Accent 2" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading Accent 2" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List Accent 2" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid Accent 2" w:uiPriority="73"/>
                    <w:lsdException w:name="Light Shading Accent 3" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List Accent 3" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid Accent 3" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1 Accent 3" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2 Accent 3" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1 Accent 3" w:uiPriority="65"/>
                    <w:lsdException w:name="Medium List 2 Accent 3" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1 Accent 3" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2 Accent 3" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3 Accent 3" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List Accent 3" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading Accent 3" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List Accent 3" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid Accent 3" w:uiPriority="73"/>
                    <w:lsdException w:name="Light Shading Accent 4" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List Accent 4" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid Accent 4" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1 Accent 4" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2 Accent 4" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1 Accent 4" w:uiPriority="65"/>
                    <w:lsdException w:name="Medium List 2 Accent 4" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1 Accent 4" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2 Accent 4" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3 Accent 4" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List Accent 4" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading Accent 4" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List Accent 4" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid Accent 4" w:uiPriority="73"/>
                    <w:lsdException w:name="Light Shading Accent 5" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List Accent 5" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid Accent 5" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1 Accent 5" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2 Accent 5" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1 Accent 5" w:uiPriority="65"/>
                    <w:lsdException w:name="Medium List 2 Accent 5" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1 Accent 5" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2 Accent 5" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3 Accent 5" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List Accent 5" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading Accent 5" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List Accent 5" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid Accent 5" w:uiPriority="73"/>
                    <w:lsdException w:name="Light Shading Accent 6" w:uiPriority="60"/>
                    <w:lsdException w:name="Light List Accent 6" w:uiPriority="61"/>
                    <w:lsdException w:name="Light Grid Accent 6" w:uiPriority="62"/>
                    <w:lsdException w:name="Medium Shading 1 Accent 6" w:uiPriority="63"/>
                    <w:lsdException w:name="Medium Shading 2 Accent 6" w:uiPriority="64"/>
                    <w:lsdException w:name="Medium List 1 Accent 6" w:uiPriority="65"/>
                    <w:lsdException w:name="Medium List 2 Accent 6" w:uiPriority="66"/>
                    <w:lsdException w:name="Medium Grid 1 Accent 6" w:uiPriority="67"/>
                    <w:lsdException w:name="Medium Grid 2 Accent 6" w:uiPriority="68"/>
                    <w:lsdException w:name="Medium Grid 3 Accent 6" w:uiPriority="69"/>
                    <w:lsdException w:name="Dark List Accent 6" w:uiPriority="70"/>
                    <w:lsdException w:name="Colorful Shading Accent 6" w:uiPriority="71"/>
                    <w:lsdException w:name="Colorful List Accent 6" w:uiPriority="72"/>
                    <w:lsdException w:name="Colorful Grid Accent 6" w:uiPriority="73"/>
                    <w:lsdException w:name="Subtle Emphasis" w:uiPriority="19" w:qFormat="1"/>
                    <w:lsdException w:name="Intense Emphasis" w:uiPriority="21" w:qFormat="1"/>
                    <w:lsdException w:name="Subtle Reference" w:uiPriority="31" w:qFormat="1"/>
                    <w:lsdException w:name="Intense Reference" w:uiPriority="32" w:qFormat="1"/>
                    <w:lsdException w:name="Book Title" w:uiPriority="33" w:qFormat="1"/>
                    <w:lsdException w:name="Bibliography" w:semiHidden="1" w:uiPriority="37" w:unhideWhenUsed="1"/>
                    <w:lsdException w:name="TOC Heading" w:semiHidden="1" w:uiPriority="39" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="Plain Table 1" w:uiPriority="41"/>
                    <w:lsdException w:name="Plain Table 2" w:uiPriority="42"/>
                    <w:lsdException w:name="Plain Table 3" w:uiPriority="43"/>
                    <w:lsdException w:name="Plain Table 4" w:uiPriority="44"/>
                    <w:lsdException w:name="Plain Table 5" w:uiPriority="45"/>
                    <w:lsdException w:name="Grid Table Light" w:uiPriority="40"/>
                    <w:lsdException w:name="Grid Table 1 Light" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful" w:uiPriority="52"/>
                    <w:lsdException w:name="Grid Table 1 Light Accent 1" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2 Accent 1" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3 Accent 1" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4 Accent 1" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark Accent 1" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful Accent 1" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful Accent 1" w:uiPriority="52"/>
                    <w:lsdException w:name="Grid Table 1 Light Accent 2" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2 Accent 2" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3 Accent 2" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4 Accent 2" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark Accent 2" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful Accent 2" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful Accent 2" w:uiPriority="52"/>
                    <w:lsdException w:name="Grid Table 1 Light Accent 3" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2 Accent 3" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3 Accent 3" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4 Accent 3" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark Accent 3" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful Accent 3" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful Accent 3" w:uiPriority="52"/>
                    <w:lsdException w:name="Grid Table 1 Light Accent 4" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2 Accent 4" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3 Accent 4" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4 Accent 4" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark Accent 4" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful Accent 4" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful Accent 4" w:uiPriority="52"/>
                    <w:lsdException w:name="Grid Table 1 Light Accent 5" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2 Accent 5" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3 Accent 5" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4 Accent 5" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark Accent 5" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful Accent 5" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful Accent 5" w:uiPriority="52"/>
                    <w:lsdException w:name="Grid Table 1 Light Accent 6" w:uiPriority="46"/>
                    <w:lsdException w:name="Grid Table 2 Accent 6" w:uiPriority="47"/>
                    <w:lsdException w:name="Grid Table 3 Accent 6" w:uiPriority="48"/>
                    <w:lsdException w:name="Grid Table 4 Accent 6" w:uiPriority="49"/>
                    <w:lsdException w:name="Grid Table 5 Dark Accent 6" w:uiPriority="50"/>
                    <w:lsdException w:name="Grid Table 6 Colorful Accent 6" w:uiPriority="51"/>
                    <w:lsdException w:name="Grid Table 7 Colorful Accent 6" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light Accent 1" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2 Accent 1" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3 Accent 1" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4 Accent 1" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark Accent 1" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful Accent 1" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful Accent 1" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light Accent 2" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2 Accent 2" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3 Accent 2" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4 Accent 2" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark Accent 2" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful Accent 2" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful Accent 2" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light Accent 3" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2 Accent 3" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3 Accent 3" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4 Accent 3" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark Accent 3" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful Accent 3" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful Accent 3" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light Accent 4" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2 Accent 4" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3 Accent 4" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4 Accent 4" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark Accent 4" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful Accent 4" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful Accent 4" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light Accent 5" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2 Accent 5" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3 Accent 5" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4 Accent 5" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark Accent 5" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful Accent 5" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful Accent 5" w:uiPriority="52"/>
                    <w:lsdException w:name="List Table 1 Light Accent 6" w:uiPriority="46"/>
                    <w:lsdException w:name="List Table 2 Accent 6" w:uiPriority="47"/>
                    <w:lsdException w:name="List Table 3 Accent 6" w:uiPriority="48"/>
                    <w:lsdException w:name="List Table 4 Accent 6" w:uiPriority="49"/>
                    <w:lsdException w:name="List Table 5 Dark Accent 6" w:uiPriority="50"/>
                    <w:lsdException w:name="List Table 6 Colorful Accent 6" w:uiPriority="51"/>
                    <w:lsdException w:name="List Table 7 Colorful Accent 6" w:uiPriority="52"/>
                </w:latentStyles>
                <w:style w:type="paragraph" w:default="1" w:styleId="a">
                    <w:name w:val="Normal"/>
                    <w:uiPriority w:val="1"/>
                    <w:qFormat/>
                    <w:rsid w:val="00C53283"/>
                    <w:pPr>
                        <w:widowControl w:val="0"/>
                        <w:autoSpaceDE w:val="0"/>
                        <w:autoSpaceDN w:val="0"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="22"/>
                        <w:lang w:val="zh-CN" w:bidi="zh-CN"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="1">
                    <w:name w:val="heading 1"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="10"/>
                    <w:uiPriority w:val="1"/>
                    <w:qFormat/>
                    <w:rsid w:val="00C53283"/>
                    <w:pPr>
                        <w:ind w:left="1788" w:right="1751"/>
                        <w:jc w:val="center"/>
                        <w:outlineLvl w:val="0"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:cs="Microsoft JhengHei"/>
                        <w:b/>
                        <w:bCs/>
                        <w:sz w:val="32"/>
                        <w:szCs w:val="32"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="2">
                    <w:name w:val="heading 2"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="20"/>
                    <w:uiPriority w:val="1"/>
                    <w:qFormat/>
                    <w:rsid w:val="00C53283"/>
                    <w:pPr>
                        <w:spacing w:line="492" w:lineRule="exact"/>
                        <w:ind w:left="418" w:hanging="300"/>
                        <w:outlineLvl w:val="1"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:cs="Microsoft JhengHei"/>
                        <w:b/>
                        <w:bCs/>
                        <w:sz w:val="30"/>
                        <w:szCs w:val="30"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="3">
                    <w:name w:val="heading 3"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="30"/>
                    <w:uiPriority w:val="1"/>
                    <w:qFormat/>
                    <w:rsid w:val="00C53283"/>
                    <w:pPr>
                        <w:ind w:left="540" w:hanging="423"/>
                        <w:outlineLvl w:val="2"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:cs="Microsoft JhengHei"/>
                        <w:b/>
                        <w:bCs/>
                        <w:sz w:val="28"/>
                        <w:szCs w:val="28"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="4">
                    <w:name w:val="heading 4"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="40"/>
                    <w:uiPriority w:val="1"/>
                    <w:qFormat/>
                    <w:rsid w:val="00C53283"/>
                    <w:pPr>
                        <w:ind w:left="880"/>
                        <w:outlineLvl w:val="3"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:cs="Microsoft JhengHei"/>
                        <w:b/>
                        <w:bCs/>
                        <w:sz w:val="24"/>
                        <w:szCs w:val="24"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:default="1" w:styleId="a0">
                    <w:name w:val="Default Paragraph Font"/>
                    <w:uiPriority w:val="1"/>
                    <w:semiHidden/>
                    <w:unhideWhenUsed/>
                </w:style>
                <w:style w:type="table" w:default="1" w:styleId="a1">
                    <w:name w:val="Normal Table"/>
                    <w:uiPriority w:val="99"/>
                    <w:semiHidden/>
                    <w:unhideWhenUsed/>
                    <w:tblPr>
                        <w:tblInd w:w="0" w:type="dxa"/>
                        <w:tblCellMar>
                            <w:top w:w="0" w:type="dxa"/>
                            <w:left w:w="108" w:type="dxa"/>
                            <w:bottom w:w="0" w:type="dxa"/>
                            <w:right w:w="108" w:type="dxa"/>
                        </w:tblCellMar>
                    </w:tblPr>
                </w:style>
                <w:style w:type="numbering" w:default="1" w:styleId="a2">
                    <w:name w:val="No List"/>
                    <w:uiPriority w:val="99"/>
                    <w:semiHidden/>
                    <w:unhideWhenUsed/>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="10">
                    <w:name w:val="标题 1 字符"/>
                    <w:basedOn w:val="a0"/>
                    <w:link w:val="1"/>
                    <w:uiPriority w:val="1"/>
                    <w:rsid w:val="00C53283"/>
                    <w:rPr>
                        <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:cs="Microsoft JhengHei"/>
                        <w:b/>
                        <w:bCs/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="32"/>
                        <w:szCs w:val="32"/>
                        <w:lang w:val="zh-CN" w:bidi="zh-CN"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="20">
                    <w:name w:val="标题 2 字符"/>
                    <w:basedOn w:val="a0"/>
                    <w:link w:val="2"/>
                    <w:uiPriority w:val="1"/>
                    <w:rsid w:val="00C53283"/>
                    <w:rPr>
                        <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:cs="Microsoft JhengHei"/>
                        <w:b/>
                        <w:bCs/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="30"/>
                        <w:szCs w:val="30"/>
                        <w:lang w:val="zh-CN" w:bidi="zh-CN"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="30">
                    <w:name w:val="标题 3 字符"/>
                    <w:basedOn w:val="a0"/>
                    <w:link w:val="3"/>
                    <w:uiPriority w:val="1"/>
                    <w:rsid w:val="00C53283"/>
                    <w:rPr>
                        <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:cs="Microsoft JhengHei"/>
                        <w:b/>
                        <w:bCs/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="28"/>
                        <w:szCs w:val="28"/>
                        <w:lang w:val="zh-CN" w:bidi="zh-CN"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="40">
                    <w:name w:val="标题 4 字符"/>
                    <w:basedOn w:val="a0"/>
                    <w:link w:val="4"/>
                    <w:uiPriority w:val="1"/>
                    <w:rsid w:val="00C53283"/>
                    <w:rPr>
                        <w:rFonts w:ascii="Microsoft JhengHei" w:eastAsia="Microsoft JhengHei" w:hAnsi="Microsoft JhengHei" w:cs="Microsoft JhengHei"/>
                        <w:b/>
                        <w:bCs/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="24"/>
                        <w:szCs w:val="24"/>
                        <w:lang w:val="zh-CN" w:bidi="zh-CN"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="table" w:customStyle="1" w:styleId="TableNormal">
                    <w:name w:val="Table Normal"/>
                    <w:uiPriority w:val="2"/>
                    <w:semiHidden/>
                    <w:unhideWhenUsed/>
                    <w:qFormat/>
                    <w:rsid w:val="00C53283"/>
                    <w:pPr>
                        <w:widowControl w:val="0"/>
                        <w:autoSpaceDE w:val="0"/>
                        <w:autoSpaceDN w:val="0"/>
                    </w:pPr>
                    <w:rPr>
                        <w:kern w:val="0"/>
                        <w:sz w:val="22"/>
                        <w:lang w:eastAsia="en-US"/>
                    </w:rPr>
                    <w:tblPr>
                        <w:tblInd w:w="0" w:type="dxa"/>
                        <w:tblCellMar>
                            <w:top w:w="0" w:type="dxa"/>
                            <w:left w:w="0" w:type="dxa"/>
                            <w:bottom w:w="0" w:type="dxa"/>
                            <w:right w:w="0" w:type="dxa"/>
                        </w:tblCellMar>
                    </w:tblPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="a3">
                    <w:name w:val="Body Text"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="a4"/>
                    <w:uiPriority w:val="1"/>
                    <w:qFormat/>
                    <w:rsid w:val="00C53283"/>
                    <w:rPr>
                        <w:sz w:val="24"/>
                        <w:szCs w:val="24"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="a4">
                    <w:name w:val="正文文本 字符"/>
                    <w:basedOn w:val="a0"/>
                    <w:link w:val="a3"/>
                    <w:uiPriority w:val="1"/>
                    <w:rsid w:val="00C53283"/>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="24"/>
                        <w:szCs w:val="24"/>
                        <w:lang w:val="zh-CN" w:bidi="zh-CN"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="a5">
                    <w:name w:val="List Paragraph"/>
                    <w:basedOn w:val="a"/>
                    <w:uiPriority w:val="34"/>
                    <w:qFormat/>
                    <w:rsid w:val="00C53283"/>
                    <w:pPr>
                        <w:ind w:left="398" w:firstLine="479"/>
                    </w:pPr>
                </w:style>
                <w:style w:type="paragraph" w:customStyle="1" w:styleId="TableParagraph">
                    <w:name w:val="Table Paragraph"/>
                    <w:basedOn w:val="a"/>
                    <w:uiPriority w:val="1"/>
                    <w:qFormat/>
                    <w:rsid w:val="00C53283"/>
                </w:style>
                <w:style w:type="paragraph" w:styleId="a6">
                    <w:name w:val="header"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="a7"/>
                    <w:uiPriority w:val="99"/>
                    <w:unhideWhenUsed/>
                    <w:rsid w:val="00C53283"/>
                    <w:pPr>
                        <w:pBdr>
                            <w:bottom w:val="single" w:sz="6" w:space="1" w:color="auto"/>
                        </w:pBdr>
                        <w:tabs>
                            <w:tab w:val="center" w:pos="4153"/>
                            <w:tab w:val="right" w:pos="8306"/>
                        </w:tabs>
                        <w:snapToGrid w:val="0"/>
                        <w:jc w:val="center"/>
                    </w:pPr>
                    <w:rPr>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="a7">
                    <w:name w:val="页眉 字符"/>
                    <w:basedOn w:val="a0"/>
                    <w:link w:val="a6"/>
                    <w:uiPriority w:val="99"/>
                    <w:rsid w:val="00C53283"/>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                        <w:lang w:val="zh-CN" w:bidi="zh-CN"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="a8">
                    <w:name w:val="footer"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="a9"/>
                    <w:uiPriority w:val="99"/>
                    <w:unhideWhenUsed/>
                    <w:rsid w:val="00C53283"/>
                    <w:pPr>
                        <w:tabs>
                            <w:tab w:val="center" w:pos="4153"/>
                            <w:tab w:val="right" w:pos="8306"/>
                        </w:tabs>
                        <w:snapToGrid w:val="0"/>
                    </w:pPr>
                    <w:rPr>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="a9">
                    <w:name w:val="页脚 字符"/>
                    <w:basedOn w:val="a0"/>
                    <w:link w:val="a8"/>
                    <w:uiPriority w:val="99"/>
                    <w:rsid w:val="00C53283"/>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体" w:eastAsia="宋体" w:hAnsi="宋体" w:cs="宋体"/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                        <w:lang w:val="zh-CN" w:bidi="zh-CN"/>
                    </w:rPr>
                </w:style>
            </w:styles>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/numbering.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.numbering+xml">
        <pkg:xmlData>
            <w:numbering
                    xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
                    xmlns:cx="http://schemas.microsoft.com/office/drawing/2014/chartex"
                    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                    xmlns:o="urn:schemas-microsoft-com:office:office"
                    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                    xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
                    xmlns:v="urn:schemas-microsoft-com:vml"
                    xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
                    xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
                    xmlns:w10="urn:schemas-microsoft-com:office:word"
                    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                    xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                    xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml"
                    xmlns:w16se="http://schemas.microsoft.com/office/word/2015/wordml/symex"
                    xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
                    xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
                    xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
                    xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" mc:Ignorable="w14 w15 w16se wp14">
                <w:abstractNum w:abstractNumId="0" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="08171871"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="9FD89DB0"/>
                    <w:lvl w:ilvl="0" w:tplc="E8FEEF82">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%1."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="739" w:hanging="262"/>
                            <w:jc w:val="right"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="default"/>
                            <w:b/>
                            <w:bCs/>
                            <w:w w:val="100"/>
                            <w:sz w:val="21"/>
                            <w:szCs w:val="21"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="F0301EB0">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1592" w:hanging="262"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="A0FA2FD2">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2444" w:hanging="262"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0512D3C4">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3296" w:hanging="262"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="ADAABF48">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="4148" w:hanging="262"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="9F96AB00">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="5000" w:hanging="262"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="D0447046">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="5852" w:hanging="262"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="9866FC12">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="6705" w:hanging="262"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="CB1CA6D8">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="7557" w:hanging="262"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="1" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="0CD169E5"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="EF9E1EDC"/>
                    <w:lvl w:ilvl="0" w:tplc="046AB56E">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="japaneseCounting"/>
                        <w:lvlText w:val="%1、"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="420" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="2" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="0F5A1B49"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="6922D01E"/>
                    <w:lvl w:ilvl="0" w:tplc="76A4DF84">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%1、"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="360" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="3" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="19A870FC"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="9E220268"/>
                    <w:lvl w:ilvl="0" w:tplc="CB807556">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%1."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="478" w:hanging="161"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="default"/>
                            <w:w w:val="100"/>
                            <w:sz w:val="19"/>
                            <w:szCs w:val="19"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="EB4423B8">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1358" w:hanging="161"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="C44421EA">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2236" w:hanging="161"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="7D3830B8">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3114" w:hanging="161"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="5162B610">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3992" w:hanging="161"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="03E4BA4E">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="4870" w:hanging="161"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="BF04734C">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="5748" w:hanging="161"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="8F16E7E2">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="6627" w:hanging="161"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="6A66598C">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="7505" w:hanging="161"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="4" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="56BA72E1"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="6ABC3C46"/>
                    <w:lvl w:ilvl="0" w:tplc="07DA734A">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%1、"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="360" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="5" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="576D3641"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="46348696"/>
                    <w:lvl w:ilvl="0" w:tplc="A0067654">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="420" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090003" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val=""/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Wingdings" w:hAnsi="Wingdings" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="04090005" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val=""/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Wingdings" w:hAnsi="Wingdings" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="04090001" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val=""/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Wingdings" w:hAnsi="Wingdings" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090003" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val=""/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Wingdings" w:hAnsi="Wingdings" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="04090005" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val=""/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Wingdings" w:hAnsi="Wingdings" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="04090001" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val=""/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Wingdings" w:hAnsi="Wingdings" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090003" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val=""/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Wingdings" w:hAnsi="Wingdings" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="04090005" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val=""/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Wingdings" w:hAnsi="Wingdings" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="6" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="79B96F16"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="FE5CD292"/>
                    <w:lvl w:ilvl="0" w:tplc="B19E8B46">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%1）"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="838" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Times New Roman" w:eastAsia="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="default"/>
                            <w:w w:val="100"/>
                            <w:sz w:val="21"/>
                            <w:szCs w:val="21"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="8662E154">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1682" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="FD5C79DE">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2524" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="CD9096B6">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3366" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="6C509DE4">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="4208" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="D37E069E">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="5050" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="2C5E8708">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="5892" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="D4185E46">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="6735" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="065C6DCE">
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val="•"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="7577" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                            <w:lang w:val="zh-CN" w:eastAsia="zh-CN" w:bidi="zh-CN"/>
                        </w:rPr>
                    </w:lvl>
                </w:abstractNum>
                <w:num w:numId="1">
                    <w:abstractNumId w:val="6"/>
                </w:num>
                <w:num w:numId="2">
                    <w:abstractNumId w:val="0"/>
                </w:num>
                <w:num w:numId="3">
                    <w:abstractNumId w:val="3"/>
                </w:num>
                <w:num w:numId="4">
                    <w:abstractNumId w:val="5"/>
                </w:num>
                <w:num w:numId="5">
                    <w:abstractNumId w:val="1"/>
                </w:num>
                <w:num w:numId="6">
                    <w:abstractNumId w:val="2"/>
                </w:num>
                <w:num w:numId="7">
                    <w:abstractNumId w:val="4"/>
                </w:num>
                <w:numIdMacAtCleanup w:val="7"/>
            </w:numbering>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/docProps/core.xml" pkg:contentType="application/vnd.openxmlformats-package.core-properties+xml" pkg:padding="256">
        <pkg:xmlData>
            <cp:coreProperties
                    xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties"
                    xmlns:dc="http://purl.org/dc/elements/1.1/"
                    xmlns:dcterms="http://purl.org/dc/terms/"
                    xmlns:dcmitype="http://purl.org/dc/dcmitype/"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                <dc:title/>
                <dc:subject/>
                <dc:creator>Xiang Jian</dc:creator>
                <cp:keywords/>
                <dc:description/>
                <cp:lastModifiedBy>甘之如饴 ?♥?悔之不及</cp:lastModifiedBy>
                <cp:revision>4</cp:revision>
                <dcterms:created xsi:type="dcterms:W3CDTF">2020-11-13T01:58:00Z</dcterms:created>
                <dcterms:modified xsi:type="dcterms:W3CDTF">2020-11-18T06:49:00Z</dcterms:modified>
            </cp:coreProperties>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/fontTable.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.wordprocessingml.fontTable+xml">
        <pkg:xmlData>
            <w:fonts
                    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
                    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
                    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
                    xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
                    xmlns:w15="http://schemas.microsoft.com/office/word/2012/wordml"
                    xmlns:w16se="http://schemas.microsoft.com/office/word/2015/wordml/symex" mc:Ignorable="w14 w15 w16se">
                <w:font w:name="Times New Roman">
                    <w:panose1 w:val="02020603050405020304"/>
                    <w:charset w:val="00"/>
                    <w:family w:val="roman"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="E0002EFF" w:usb1="C000785B" w:usb2="00000009" w:usb3="00000000" w:csb0="000001FF" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Wingdings">
                    <w:panose1 w:val="05000000000000000000"/>
                    <w:charset w:val="02"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="00000000" w:usb1="10000000" w:usb2="00000000" w:usb3="00000000" w:csb0="80000000" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Calibri">
                    <w:panose1 w:val="020F0502020204030204"/>
                    <w:charset w:val="00"/>
                    <w:family w:val="swiss"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="E4002EFF" w:usb1="C000247B" w:usb2="00000009" w:usb3="00000000" w:csb0="000001FF" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="宋体">
                    <w:altName w:val="SimSun"/>
                    <w:panose1 w:val="02010600030101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="00000003" w:usb1="288F0000" w:usb2="00000016" w:usb3="00000000" w:csb0="00040001" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Microsoft JhengHei">
                    <w:panose1 w:val="020B0604030504040204"/>
                    <w:charset w:val="88"/>
                    <w:family w:val="swiss"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="000002A7" w:usb1="28CF4400" w:usb2="00000016" w:usb3="00000000" w:csb0="00100009" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="黑体">
                    <w:altName w:val="SimHei"/>
                    <w:panose1 w:val="02010609060101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="modern"/>
                    <w:pitch w:val="fixed"/>
                    <w:sig w:usb0="800002BF" w:usb1="38CF7CFA" w:usb2="00000016" w:usb3="00000000" w:csb0="00040001" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="仿宋">
                    <w:panose1 w:val="02010609060101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="modern"/>
                    <w:pitch w:val="fixed"/>
                    <w:sig w:usb0="800002BF" w:usb1="38CF7CFA" w:usb2="00000016" w:usb3="00000000" w:csb0="00040001" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="楷体">
                    <w:panose1 w:val="02010609060101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="modern"/>
                    <w:pitch w:val="fixed"/>
                    <w:sig w:usb0="800002BF" w:usb1="38CF7CFA" w:usb2="00000016" w:usb3="00000000" w:csb0="00040001" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Calibri Light">
                    <w:panose1 w:val="020F0302020204030204"/>
                    <w:charset w:val="00"/>
                    <w:family w:val="swiss"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="E4002EFF" w:usb1="C000247B" w:usb2="00000009" w:usb3="00000000" w:csb0="000001FF" w:csb1="00000000"/>
                </w:font>
            </w:fonts>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/docProps/app.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.extended-properties+xml" pkg:padding="256">
        <pkg:xmlData>
            <Properties
                    xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties"
                    xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes">
                <Template>Normal.dotm</Template>
                <TotalTime>16</TotalTime>
                <Pages>4</Pages>
                <Words>324</Words>
                <Characters>1849</Characters>
                <Application>Microsoft Office Word</Application>
                <DocSecurity>0</DocSecurity>
                <Lines>15</Lines>
                <Paragraphs>4</Paragraphs>
                <ScaleCrop>false</ScaleCrop>
                <Company>微软中国</Company>
                <LinksUpToDate>false</LinksUpToDate>
                <CharactersWithSpaces>2169</CharactersWithSpaces>
                <SharedDoc>false</SharedDoc>
                <HyperlinksChanged>false</HyperlinksChanged>
                <AppVersion>16.0000</AppVersion>
            </Properties>
        </pkg:xmlData>
    </pkg:part>
</pkg:package>