[#ftl]
[#assign names = ["团队负责人", "审核人", "执笔人", "审批人", "责任教授", "核准院长"]]
[#assign TEXT = 1, NUMBER = 2, TOTAL = 3, INDICATION = 4,INDICTIONPORT=5,show=0,merge=0]
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
                    [#if data??]
                        <!-- 大标题 -->
                        [#if data.name??]
                            <w:p w:rsidR="002E5D6B" w:rsidRPr="002364E3" w:rsidRDefault="00567F40" w:rsidP="002E5D6B">
                                <w:pPr>
                                    <w:pStyle w:val="-"/>
                                    <w:ind w:firstLine="0"/>
                                    <w:jc w:val="center"/>
                                    <w:rPr>
                                        <w:b/>
                                        <w:bCs/>
                                        <w:color w:val="auto"/>
                                        <w:sz w:val="21"/>
                                        <w:szCs w:val="21"/>
                                    </w:rPr>
                                </w:pPr>
                                [#if data.bookMarkIdOne?? && data.courseOutlineIndexOne??]
                                    <w:r>
                                        <w:rPr>
                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                            <w:b/>
                                            <w:bCs/>
                                            <w:sz w:val="32"/>
                                            <w:szCs w:val="32"/>
                                        </w:rPr>
                                        <w:br w:type="page"/>
                                    </w:r>
                                [/#if]
                                <w:bookmarkStart w:id="${data.bookMarkIdOne!'0'}" w:name="_Toc${data.courseOutlineIndexOne!''}"/>
                                <w:r w:rsidRPr="00567F40">
                                    <w:rPr>
                                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                        <w:b/>
                                        <w:bCs/>
                                        <w:sz w:val="32"/>
                                        <w:szCs w:val="32"/>
                                    </w:rPr>
                                    <w:t>${data.name} </w:t>
                                </w:r>
                                <w:bookmarkEnd w:id="${data.bookMarkIdOne!'0'}"/>
                            </w:p>
                        [/#if]

                        [#if data.courseInfos??]
                            [#assign creditHours = ""]
                            [#list data.courseInfos as courseInfo]
                                [#if "学时" == courseInfo.name && courseInfo.content??]
                                    [#assign creditHours = courseInfo.content]
                                [/#if]
                            [/#list]
                        [/#if]

                        [#assign creditHoursCompose = []]
                        [#if data.theoryHours?? && data.theoryHours>0]
                            [#assign theoryHoursStr = "理论：" + data.theoryHours?string]
                            [#assign creditHoursCompose = creditHoursCompose + [theoryHoursStr]]
                        [/#if]

                        [#if data.experimentHours?? && data.experimentHours>0]
                            [#assign experimentHoursStr = "实验：" + data.experimentHours?string]
                            [#assign creditHoursCompose = creditHoursCompose + [experimentHoursStr]]
                        [/#if]

                        [#if data.practiceHours?? && data.practiceHours>0]
                            [#assign practiceHoursStr = "实践：" + data.practiceHours?string]
                            [#assign creditHoursCompose = creditHoursCompose + [practiceHoursStr]]
                        [/#if]

                        [#if data.dicussHours?? && data.dicussHours>0]
                            [#assign dicussHoursStr = "研讨："+ data.dicussHours?string]
                            [#assign creditHoursCompose = creditHoursCompose + [dicussHoursStr]]
                        [/#if]

                        [#if data.exercisesHours?? && data.exercisesHours>0]
                            [#assign exercisesHoursStr = "习题：" + data.exercisesHours?string]
                            [#assign creditHoursCompose = creditHoursCompose + [exercisesHoursStr]]
                        [/#if]

                        <!-- 课程信息列表 -->
                        [#if data.courseInfos??]
                            [#list data.courseInfos as couresInfo]
                                [#if couresInfo.name?? && (!names?seq_contains(couresInfo.name))]

                                    [#if couresInfo.name == "学时"]
                                    [#elseif couresInfo.name == "学分"]
                                        <w:p w:rsidR="002364E3" w:rsidRPr="00866D64" w:rsidRDefault="002364E3" w:rsidP="002364E3">
                                            <w:pPr>
                                                <w:adjustRightInd w:val="0"/>
                                                <w:ind w:firstLineChars="200" w:firstLine="422"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="00393F8F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="宋体" w:hint="eastAsia"/>
                                                    <w:b/>
                                                    <w:bCs/>
                                                </w:rPr>
                                                <w:t>学分/学时：</w:t>
                                            </w:r>
                                            <w:r w:rsidR="00567F40" w:rsidRPr="00567F40">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                                </w:rPr>
                                                <w:t>
                                                    [#if couresInfo.content??]${couresInfo.content}[/#if]/[#if creditHours != ""]${creditHours}[/#if][#list creditHoursCompose]（[#items as creaditHours]${creaditHours}[#sep]，[/#items]）[#else][/#list]
                                                </w:t>
                                            </w:r>
                                            <w:r w:rsidRPr="00866D64">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="宋体" w:hint="eastAsia"/>
                                                    <w:bCs/>
                                                    <w:color w:val="FF0000"/>
                                                </w:rPr>
                                                <w:t xml:space="preserve"></w:t>
                                            </w:r>
                                        </w:p>
                                    [#else]
                                        <w:p w:rsidR="002364E3" w:rsidRPr="00866D64" w:rsidRDefault="002364E3" w:rsidP="002364E3">
                                            <w:pPr>
                                                <w:adjustRightInd w:val="0"/>
                                                <w:ind w:firstLineChars="200" w:firstLine="422"/>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="00393F8F">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="宋体" w:hint="eastAsia"/>
                                                    <w:b/>
                                                    <w:bCs/>
                                                </w:rPr>
                                                <w:t>${couresInfo.name}：</w:t>
                                            </w:r>
                                            <w:r w:rsidR="00567F40" w:rsidRPr="00567F40">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                                </w:rPr>
                                                <w:t>[#if couresInfo.content??]${couresInfo.content}[/#if]</w:t>
                                            </w:r>
                                            <w:r w:rsidRPr="00866D64">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="宋体" w:hint="eastAsia"/>
                                                    <w:bCs/>
                                                    <w:color w:val="FF0000"/>
                                                </w:rPr>
                                                <w:t xml:space="preserve"></w:t>
                                            </w:r>
                                        </w:p>
                                    [/#if]
                                [/#if]
                            [/#list]
                        [/#if]
                        <w:tbl>
                            <w:tblPr>
                                <w:tblW w:w="0" w:type="auto"/>
                                <w:tblInd w:w="468" w:type="dxa"/>
                                <w:tblLook w:val="01E0" w:firstRow="1" w:lastRow="1" w:firstColumn="1" w:lastColumn="1" w:noHBand="0" w:noVBand="0"/>
                            </w:tblPr>
                            <w:tblGrid>
                                <w:gridCol w:w="1640"/>
                                <w:gridCol w:w="2600"/>
                                <w:gridCol w:w="1076"/>
                                <w:gridCol w:w="2344"/>
                            </w:tblGrid>
                            <w:tr w:rsidR="00CA327E" w:rsidRPr="008D4C72" w:rsidTr="00B770D0">
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="1640" w:type="dxa"/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00CA327E" w:rsidRPr="008D4C72" w:rsidRDefault="00CA327E" w:rsidP="00B770D0">
                                        <w:pPr>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="008D4C72">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t>
                                                [#if data.courseInfos??]
                                                    [#list data.courseInfos as couresInfo]
                                                        [#if "团队负责人" == couresInfo.name && couresInfo.content??]
                                                            团队负责人：
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                            </w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="2700" w:type="dxa"/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00CA327E" w:rsidRPr="008D4C72" w:rsidRDefault="00CA327E" w:rsidP="00B770D0">
                                        <w:pPr>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="003219BF">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t>
                                                [#if data.courseInfos??]
                                                    [#list data.courseInfos as couresInfo]
                                                        [#if "团队负责人" == couresInfo.name && couresInfo.content??]
                                                            ${couresInfo.content}
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                            </w:t>
                                        </w:r>
                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t xml:space="preserve"></w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="1600" w:type="dxa"/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00CA327E" w:rsidRPr="008D4C72" w:rsidRDefault="00CA327E" w:rsidP="00B770D0">
                                        <w:pPr>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="008D4C72">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t>
                                                [#if data.auditorName??]审核人：[/#if]
                                            </w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="2700" w:type="dxa"/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00CA327E" w:rsidRPr="008D4C72" w:rsidRDefault="00CA327E" w:rsidP="00B770D0">
                                        <w:pPr>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="003219BF">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t>
                                                [#if data.auditorName??]${data.auditorName}[/#if]
                                            </w:t>
                                        </w:r>
                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t xml:space="preserve"></w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                            </w:tr>

                            [#----]
                            <w:tr w:rsidR="00CA327E" w:rsidRPr="008D4C72" w:rsidTr="00B770D0">
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="1600" w:type="dxa"/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00CA327E" w:rsidRPr="008D4C72" w:rsidRDefault="00CA327E" w:rsidP="00B770D0">
                                        <w:pPr>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="008D4C72">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t>
                                                [#if data.authorName??]执笔人 ：[/#if]
                                            </w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="2700" w:type="dxa"/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00CA327E" w:rsidRPr="008D4C72" w:rsidRDefault="00CA327E" w:rsidP="00B770D0">
                                        <w:pPr>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="003219BF">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t>
                                                [#if data.authorName??]${data.authorName}[/#if]
                                            </w:t>
                                        </w:r>
                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t xml:space="preserve"></w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="1600" w:type="dxa"/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00CA327E" w:rsidRPr="008D4C72" w:rsidRDefault="00CA327E" w:rsidP="00B770D0">
                                        <w:pPr>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="008D4C72">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t>
                                                [#if data.courseInfos??]
                                                    [#list data.courseInfos as couresInfo]
                                                        [#if "审批人" == couresInfo.name && couresInfo.content??]
                                                            审批人：
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                            </w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="2700" w:type="dxa"/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00CA327E" w:rsidRPr="008D4C72" w:rsidRDefault="00CA327E" w:rsidP="00B770D0">
                                        <w:pPr>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="003219BF">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t>
                                                [#if data.courseInfos??]
                                                    [#list data.courseInfos as couresInfo]
                                                        [#if "审批人" == couresInfo.name && couresInfo.content??]
                                                            ${couresInfo.content}
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                            </w:t>
                                        </w:r>
                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t xml:space="preserve"></w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                            </w:tr>


                            [#if data.courseInfos??]
                            [#list data.courseInfos as couresInfo]
                            [#if "责任教授" == couresInfo.name || "核准院长" == couresInfo.name]
                            <w:tr w:rsidR="00CA327E" w:rsidRPr="003219BF" w:rsidTr="00B770D0">

                                <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="1600" w:type="dxa"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00CA327E" w:rsidRPr="008D4C72" w:rsidRDefault="00CA327E" w:rsidP="00B770D0">
                                            <w:pPr>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                    <w:b/>
                                                    <w:bCs/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r w:rsidRPr="008D4C72">
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                    <w:b/>
                                                    <w:bCs/>
                                                </w:rPr>
                                                <w:t>
                                                    [#if data.courseInfos??]
                                                    [#list data.courseInfos as couresInfo]
                                                        [#if "责任教授" == couresInfo.name && couresInfo.content??]
                                                            责任教授：
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                                </w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>
                                    <w:tc>
                                        <w:tcPr>
                                            <w:tcW w:w="2700" w:type="dxa"/>
                                        </w:tcPr>
                                        <w:p w:rsidR="00CA327E" w:rsidRPr="003219BF" w:rsidRDefault="00CA327E" w:rsidP="00B770D0">
                                            <w:pPr>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                    <w:bCs/>
                                                </w:rPr>
                                            </w:pPr>
                                            <w:r>
                                                <w:rPr>
                                                    <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                </w:rPr>
                                                <w:t>
                                                    [#if data.courseInfos??]
                                                    [#list data.courseInfos as couresInfo]
                                                        [#if "责任教授" == couresInfo.name && couresInfo.content??]
                                                            ${couresInfo.content}
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                                </w:t>
                                            </w:r>
                                        </w:p>
                                    </w:tc>



                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="1600" w:type="dxa"/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00CA327E" w:rsidRPr="008D4C72" w:rsidRDefault="00CA327E" w:rsidP="00B770D0">
                                        <w:pPr>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="008D4C72">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t>
                                                [#if data.courseInfos??]
                                                    [#list data.courseInfos as couresInfo]
                                                        [#if "核准院长" == couresInfo.name && couresInfo.content??]
                                                            核准院长：
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                            </w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>
                                <w:tc>
                                    <w:tcPr>
                                        <w:tcW w:w="2344" w:type="dxa"/>
                                    </w:tcPr>
                                    <w:p w:rsidR="00CA327E" w:rsidRPr="003219BF" w:rsidRDefault="00CA327E" w:rsidP="00B770D0">
                                        <w:pPr>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:bCs/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="003219BF">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t>
                                                [#if data.courseInfos??]
                                                    [#list data.courseInfos as couresInfo]
                                                        [#if "核准院长" == couresInfo.name && couresInfo.content??]
                                                            ${couresInfo.content}
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                            </w:t>
                                        </w:r>
                                        <w:r w:rsidRPr="003219BF">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t xml:space="preserve"></w:t>
                                        </w:r>
                                    </w:p>
                                </w:tc>

                            </w:tr>
                            [/#if]
                            [/#list]

                            [/#if]

                        </w:tbl>
                        <w:p w:rsidR="002E5D6B" w:rsidRPr="00393F8F" w:rsidRDefault="002E5D6B" w:rsidP="002E5D6B">
                            <w:pPr>
                                <w:ind w:firstLineChars="200" w:firstLine="422"/>
                                <w:rPr>
                                    <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="eastAsia"/>
                                    <w:b/>
                                    <w:bCs/>
                                </w:rPr>
                            </w:pPr>
                        </w:p>
                        <!-- 模块内容列表 -->
                        [#if data.modules??]
                            [#list data.modules as module]
                                [#if module.title??]
                                    <w:p w:rsidR="00AD2E02" w:rsidRPr="00393F8F" w:rsidRDefault="00AD2E02" w:rsidP="00AD2E02">
                                        <w:pPr>
                                            <w:adjustRightInd w:val="0"/>
                                            <w:ind w:firstLineChars="196" w:firstLine="413"/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="00393F8F">
                                            <w:rPr>
                                                <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="宋体" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:bCs/>
                                            </w:rPr>
                                            <w:t>[#if module.moduleIndex??]${module.moduleIndex}、[/#if]${module.title}</w:t>
                                        </w:r>
                                    </w:p>
                                [/#if]
                                [#if module.isExistMainContent && module.mainContent??]
                                    [#if module.mainContent?is_string && module.mainContent?index_of("<w:") != -1 && module.mainContent?index_of("</w:") != -1]
                                        ${module.mainContent}
                                    [#else]
                                        [#assign mainContentMap = module.mainContent]
                                        [#if mainContentMap?is_hash]
                                            [#if mainContentMap.isNonChinese == 0]
                                                <w:p w:rsidR="00CF1355" w:rsidRPr="00F96D02" w:rsidRDefault="00CF1355" w:rsidP="00CF1355">
                                                    <w:pPr>
                                                        <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                        <w:rPr>
                                                            <w:color w:val="000000"/>
                                                        </w:rPr>
                                                    </w:pPr>
                                                    <w:r>
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                        </w:rPr>
                                                        <w:t></w:t>
                                                    </w:r>
                                                </w:p>
                                            [#else]
                                                [#assign paragraphDataList = mainContentMap.paragraphDataList]
                                                [#if paragraphDataList?? && paragraphDataList?size >0]
                                                    [#list paragraphDataList as paragraph]
                                                        <w:p w:rsidR="00CF1355" w:rsidRPr="00F96D02" w:rsidRDefault="00CF1355" w:rsidP="00CF1355">
                                                            <w:pPr>
                                                                <w:pStyle w:val="ad"/>
                                                                <w:spacing w:line="300" w:lineRule="auto"/>
                                                                <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                                <w:rPr>
                                                                    <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                </w:rPr>
                                                            </w:pPr>
                                                            [#if paragraph?? && paragraph?size > 0]
                                                                [#list paragraph as sentence]
                                                                    [#if sentence?? && sentence.isNonChinese??]
                                                                        [#if sentence.isNonChinese == 1]
                                                                            <w:r w:rsidRPr="00833795">
                                                                                <w:rPr>
                                                                                    <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                                                                </w:rPr>
                                                                                <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                            </w:r>
                                                                        [#else ]
                                                                            <w:r>
                                                                                <w:rPr>
                                                                                    <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
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
                                [#if module.isMainContentSupport?? && module.isMainContentSupport]
                                    <w:p w:rsidR="00CF1355" w:rsidRDefault="00CF1355" w:rsidP="00CF1355">
                                        <w:pPr>
                                            <w:tabs>
                                                <w:tab w:val="left" w:pos="4680"/>
                                                <w:tab w:val="left" w:pos="4860"/>
                                            </w:tabs>
                                            <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                            <w:rPr>
                                                <w:rFonts w:hint="eastAsia"/>
                                                <w:i/>
                                                <w:color w:val="0000FF"/>
                                                <w:szCs w:val="20"/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r>
                                            <w:rPr>
                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                <w:color w:val="000000"/>
                                            </w:rPr>
                                            <w:t>重点支持课程目标：[#if module.mainContentIndications??][#list module.mainContentIndications as indication]CO${indication.sort} ${indication.content}[#sep]，[/#list][/#if]</w:t>
                                        </w:r>
                                    </w:p>
                                [/#if]
                                [#--这个暂时去除，如果用的话，改为1即可--]
                                [#if module_index == 15]
                                    <w:p w14:paraId="05DAC834" w14:textId="01824315" w:rsidR="000145EC" w:rsidRPr="00180658" w:rsidRDefault="00180658" w:rsidP="00180658">
                                        <w:pPr>
                                            <w:jc w:val="center"/>
                                            <w:rPr>
                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:sz w:val="21"/>
                                                <w:szCs w:val="21"/>
                                            </w:rPr>
                                        </w:pPr>
                                        <w:r w:rsidRPr="00180658">
                                            <w:rPr>
                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                <w:b/>
                                                <w:sz w:val="21"/>
                                                <w:szCs w:val="21"/>
                                            </w:rPr>
                                            <w:t>表1 毕业要求指标点和课程目标的能力要求</w:t>
                                        </w:r>
                                    </w:p>
                                    <w:tbl>
                                        <w:tblPr>
                                            <w:tblW w:w="8365" w:type="dxa"/>
                                            <w:jc w:val="center"/>
                                            <w:tblBorders>
                                                <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:insideH w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                <w:insideV w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                            </w:tblBorders>
                                            <w:tblLook w:val="04A0" w:firstRow="1" w:lastRow="0" w:firstColumn="1" w:lastColumn="0" w:noHBand="0" w:noVBand="1"/>
                                        </w:tblPr>
                                        <w:tblGrid>

                                        </w:tblGrid>
                                        <w:tr w:rsidR="00180658" w:rsidRPr="00180658" w14:paraId="1F05CD43" w14:textId="77777777" w:rsidTr="00C334C8">
                                            <w:trPr>
                                                <w:jc w:val="center"/>
                                            </w:trPr>
                                            <w:tc>
                                                <w:tcPr>
                                                    <w:tcW w:w="730" w:type="dxa"/>
                                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                    <w:vAlign w:val="center"/>
                                                </w:tcPr>
                                                <w:p w14:paraId="20F476ED" w14:textId="77777777" w:rsidR="00180658" w:rsidRPr="00180658" w:rsidRDefault="00180658" w:rsidP="00180658">
                                                    <w:pPr>
                                                        <w:jc w:val="center"/>
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                            <w:b/>
                                                            <w:sz w:val="18"/>
                                                            <w:szCs w:val="18"/>
                                                        </w:rPr>
                                                    </w:pPr>
                                                    <w:r w:rsidRPr="00180658">
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                            <w:b/>
                                                            <w:sz w:val="18"/>
                                                            <w:szCs w:val="18"/>
                                                        </w:rPr>
                                                        <w:t>序号</w:t>
                                                    </w:r>
                                                </w:p>
                                            </w:tc>
                                            <w:tc>
                                                <w:tcPr>
                                                    <w:tcW w:w="0" w:type="auto"/>
                                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                    <w:vAlign w:val="center"/>
                                                </w:tcPr>
                                                <w:p w14:paraId="5D831AAB" w14:textId="77777777" w:rsidR="00180658" w:rsidRPr="00180658" w:rsidRDefault="00180658" w:rsidP="00180658">
                                                    <w:pPr>
                                                        <w:jc w:val="center"/>
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                            <w:b/>
                                                            <w:sz w:val="18"/>
                                                            <w:szCs w:val="18"/>
                                                        </w:rPr>
                                                    </w:pPr>
                                                    <w:r w:rsidRPr="00180658">
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                            <w:b/>
                                                            <w:sz w:val="18"/>
                                                            <w:szCs w:val="18"/>
                                                        </w:rPr>
                                                        <w:t>毕业要求指标点</w:t>
                                                    </w:r>
                                                </w:p>
                                            </w:tc>
                                            <w:tc>
                                                <w:tcPr>
                                                    <w:tcW w:w="0" w:type="auto"/>
                                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                    <w:vAlign w:val="center"/>
                                                </w:tcPr>
                                                <w:p w14:paraId="09A2F149" w14:textId="77777777" w:rsidR="00180658" w:rsidRPr="00180658" w:rsidRDefault="00180658" w:rsidP="00180658">
                                                    <w:pPr>
                                                        <w:jc w:val="center"/>
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                            <w:b/>
                                                            <w:sz w:val="18"/>
                                                            <w:szCs w:val="18"/>
                                                        </w:rPr>
                                                    </w:pPr>
                                                    <w:r w:rsidRPr="00180658">
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                            <w:b/>
                                                            <w:sz w:val="18"/>
                                                            <w:szCs w:val="18"/>
                                                        </w:rPr>
                                                        <w:t>课程目标</w:t>
                                                    </w:r>
                                                </w:p>
                                            </w:tc>
                                            <w:tc>
                                                <w:tcPr>
                                                    <w:tcW w:w="0" w:type="auto"/>
                                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                    <w:vAlign w:val="center"/>
                                                </w:tcPr>
                                                <w:p w14:paraId="4B0F24DB" w14:textId="77777777" w:rsidR="00180658" w:rsidRPr="00180658" w:rsidRDefault="00180658" w:rsidP="00180658">
                                                    <w:pPr>
                                                        <w:jc w:val="center"/>
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                            <w:b/>
                                                            <w:sz w:val="18"/>
                                                            <w:szCs w:val="18"/>
                                                        </w:rPr>
                                                    </w:pPr>
                                                    <w:r w:rsidRPr="00180658">
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                            <w:b/>
                                                            <w:sz w:val="18"/>
                                                            <w:szCs w:val="18"/>
                                                        </w:rPr>
                                                        <w:t>备注</w:t>
                                                    </w:r>
                                                </w:p>
                                            </w:tc>
                                        </w:tr>
                                        [#if data.indicationTargetList??]
                                            [#list data.indicationTargetList as indicationTarget]
                                                [#if indicationTarget.courseTargets??]
                                                    [#list indicationTarget.courseTargets as courseTarget]
                                                        [#if courseTarget?is_first]
                                                            <w:tr w:rsidR="00180658" w:rsidRPr="00180658" w14:paraId="1D7A4800" w14:textId="77777777" w:rsidTr="00065749">
                                                                <w:trPr>
                                                                    <w:jc w:val="center"/>
                                                                </w:trPr>
                                                                <w:tc>
                                                                    <w:tcPr>
                                                                        <w:tcW w:w="730" w:type="dxa"/>
                                                                        [#if indicationTarget.courseTargets?size > 1]
                                                                            <w:vMerge w:val="restart"/>
                                                                        [/#if]
                                                                        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                        <w:vAlign w:val="center"/>
                                                                    </w:tcPr>
                                                                    <w:p w14:paraId="3CA045CD" w14:textId="77777777" w:rsidR="00180658" w:rsidRPr="00180658" w:rsidRDefault="00180658" w:rsidP="00650EF9">
                                                                        <w:pPr>
                                                                            <w:jc w:val="center"/>
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                        </w:pPr>
                                                                        <w:r w:rsidRPr="00180658">
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                            <w:t>${indicationTarget?counter}</w:t>
                                                                        </w:r>
                                                                    </w:p>
                                                                </w:tc>
                                                                <w:tc>
                                                                    <w:tcPr>
                                                                        <w:tcW w:w="0" w:type="auto"/>
                                                                        [#if indicationTarget.courseTargets?size > 1]
                                                                            <w:vMerge w:val="restart"/>
                                                                        [/#if]
                                                                        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                        <w:vAlign w:val="center"/>
                                                                    </w:tcPr>
                                                                    <w:p w14:paraId="6BA2A7BB" w14:textId="0D03210E" w:rsidR="00180658" w:rsidRPr="008265D2" w:rsidRDefault="00180658" w:rsidP="00180658">
                                                                        <w:pPr>
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                        </w:pPr>
                                                                        <w:r w:rsidRPr="008265D2">
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                            <w:t>${indicationTarget.graduateIndexNum!''}-${indicationTarget.indicationIndexNum!''}.${indicationTarget.indicationContent!''}</w:t>
                                                                        </w:r>
                                                                    </w:p>
                                                                </w:tc>
                                                                <w:tc>
                                                                    <w:tcPr>
                                                                        <w:tcW w:w="0" w:type="auto"/>
                                                                        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                        <w:vAlign w:val="center"/>
                                                                    </w:tcPr>
                                                                    <w:p w14:paraId="5A099E1A" w14:textId="6B996220" w:rsidR="00180658" w:rsidRPr="008265D2" w:rsidRDefault="00180658" w:rsidP="00650EF9">
                                                                        <w:pPr>
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                        </w:pPr>
                                                                        [#if courseTarget.sort??]
                                                                            <w:r w:rsidRPr="008265D2">
                                                                                <w:rPr>
                                                                                    <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                    <w:sz w:val="18"/>
                                                                                    <w:szCs w:val="18"/>
                                                                                </w:rPr>
                                                                                <w:t>CO${courseTarget.sort!''}</w:t>
                                                                            </w:r>
                                                                        [/#if]
                                                                        <w:r w:rsidRPr="008265D2">
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                            <w:t>${courseTarget.content!''}</w:t>
                                                                        </w:r>
                                                                    </w:p>
                                                                </w:tc>
                                                                <w:tc>
                                                                    <w:tcPr>
                                                                        <w:tcW w:w="0" w:type="auto"/>
                                                                        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                        <w:vAlign w:val="center"/>
                                                                    </w:tcPr>
                                                                    <w:p w14:paraId="3E033E50" w14:textId="43180EF8" w:rsidR="00180658" w:rsidRPr="008265D2" w:rsidRDefault="00180658" w:rsidP="00180658">
                                                                        <w:pPr>
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                        </w:pPr>
                                                                        <w:r w:rsidRPr="008265D2">
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                            <w:t></w:t>
                                                                        </w:r>
                                                                    </w:p>
                                                                </w:tc>
                                                            </w:tr>
                                                        [#else]
                                                            <w:tr w:rsidR="00180658" w:rsidRPr="00180658" w14:paraId="44FC639D" w14:textId="77777777" w:rsidTr="00180658">
                                                                <w:trPr>
                                                                    <w:jc w:val="center"/>
                                                                </w:trPr>
                                                                <w:tc>
                                                                    <w:tcPr>
                                                                        <w:tcW w:w="730" w:type="dxa"/>
                                                                        <w:vMerge/>
                                                                        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                        <w:vAlign w:val="center"/>
                                                                    </w:tcPr>
                                                                    <w:p w14:paraId="7728DFAB" w14:textId="77777777" w:rsidR="00180658" w:rsidRPr="00180658" w:rsidRDefault="00180658" w:rsidP="00650EF9">
                                                                        <w:pPr>
                                                                            <w:jc w:val="center"/>
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                        </w:pPr>
                                                                    </w:p>
                                                                </w:tc>
                                                                <w:tc>
                                                                    <w:tcPr>
                                                                        <w:tcW w:w="0" w:type="auto"/>
                                                                        <w:vMerge/>
                                                                        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                        <w:vAlign w:val="center"/>
                                                                    </w:tcPr>
                                                                    <w:p w14:paraId="7619A1BF" w14:textId="77777777" w:rsidR="00180658" w:rsidRPr="008265D2" w:rsidRDefault="00180658" w:rsidP="00650EF9">
                                                                        <w:pPr>
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                        </w:pPr>
                                                                    </w:p>
                                                                </w:tc>
                                                                <w:tc>
                                                                    <w:tcPr>
                                                                        <w:tcW w:w="0" w:type="auto"/>
                                                                        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                        <w:vAlign w:val="center"/>
                                                                    </w:tcPr>
                                                                    <w:p w14:paraId="5A099E1A" w14:textId="6B996220" w:rsidR="00180658" w:rsidRPr="008265D2" w:rsidRDefault="00180658" w:rsidP="00650EF9">
                                                                        <w:pPr>
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                        </w:pPr>
                                                                        [#if courseTarget.sort??]
                                                                            <w:r w:rsidRPr="008265D2">
                                                                                <w:rPr>
                                                                                    <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                    <w:sz w:val="18"/>
                                                                                    <w:szCs w:val="18"/>
                                                                                </w:rPr>
                                                                                <w:t>CO${courseTarget.sort!''}</w:t>
                                                                            </w:r>
                                                                        [/#if]
                                                                        <w:r w:rsidRPr="008265D2">
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                            <w:t>${courseTarget.content!''}</w:t>
                                                                        </w:r>
                                                                    </w:p>
                                                                </w:tc>
                                                                <w:tc>
                                                                    <w:tcPr>
                                                                        <w:tcW w:w="0" w:type="auto"/>
                                                                        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                        <w:vAlign w:val="center"/>
                                                                    </w:tcPr>
                                                                    <w:p w14:paraId="6BB62027" w14:textId="77777777" w:rsidR="00180658" w:rsidRPr="00180658" w:rsidRDefault="00180658" w:rsidP="00650EF9">
                                                                        <w:pPr>
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                        </w:pPr>
                                                                        <w:r w:rsidRPr="008265D2">
                                                                            <w:rPr>
                                                                                <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                            <w:t></w:t>
                                                                        </w:r>
                                                                    </w:p>
                                                                </w:tc>
                                                            </w:tr>
                                                        [/#if]
                                                    [/#list]
                                                [/#if]
                                            [/#list]
                                        [/#if]
                                    </w:tbl>
                                [/#if]
                                <!-- 教学内容列表 -->
                                [#if  module.teachingContents??]
                                    [#list module.teachingContents as teachingContent]
                                        [#if teachingContent.teachingContent?? && teachingContent.hours??]
                                            <w:p w:rsidR="00CF1355" w:rsidRPr="00E738A4" w:rsidRDefault="00CF1355" w:rsidP="00CA327E">
                                                <w:pPr>
                                                    <w:ind w:firstLine="420"/>
                                                    <w:rPr>
                                                        <w:rFonts w:hint="eastAsia"/>
                                                    </w:rPr>
                                                </w:pPr>
                                                <w:r w:rsidRPr="00E738A4">
                                                    <w:t>${teachingContent_index + 1}. ${teachingContent.teachingContent}  ${teachingContent.hours}</w:t>
                                                </w:r>
                                            </w:p>
                                        [/#if]
                                        [#if teachingContent.basicRequirement??]
                                            [#if teachingContent.basicRequirement?is_string && teachingContent.basicRequirement?index_of("<w:") != -1 && teachingContent.basicRequirement?index_of("</w:") != -1]
                                                ${teachingContent.basicRequirement}
                                            [#else ]
                                                [#assign mainContentMap = teachingContent.basicRequirement]
                                                [#if mainContentMap?is_hash]
                                                    [#if mainContentMap.isNonChinese == 0]
                                                        <w:p w:rsidR="00CF1355" w:rsidRPr="00F96D02" w:rsidRDefault="00CF1355" w:rsidP="00CF1355">
                                                            <w:pPr>
                                                                <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                                <w:rPr>
                                                                    <w:color w:val="000000"/>
                                                                </w:rPr>
                                                            </w:pPr>
                                                            <w:r>
                                                                <w:rPr>
                                                                    <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                </w:rPr>
                                                                <w:t></w:t>
                                                            </w:r>
                                                        </w:p>
                                                    [#else]
                                                        [#assign paragraphDataList = mainContentMap.paragraphDataList]
                                                        [#if paragraphDataList?? && paragraphDataList?size >0]
                                                            [#list paragraphDataList as paragraph]
                                                                <w:p w:rsidR="00CF1355" w:rsidRPr="00F96D02" w:rsidRDefault="00CF1355" w:rsidP="00CF1355">
                                                                    <w:pPr>
                                                                        <w:pStyle w:val="ad"/>
                                                                        <w:spacing w:line="300" w:lineRule="auto"/>
                                                                        <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                                        <w:rPr>
                                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                        </w:rPr>
                                                                    </w:pPr>
                                                                    [#if paragraph?? && paragraph?size > 0]
                                                                        [#list paragraph as sentence]
                                                                            [#if sentence?? && sentence.isNonChinese??]
                                                                                [#if sentence.isNonChinese == 1]
                                                                                    <w:r w:rsidRPr="00833795">
                                                                                        <w:rPr>
                                                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                                                                        </w:rPr>
                                                                                        <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                                    </w:r>
                                                                                [#else ]
                                                                                    <w:r>
                                                                                        <w:rPr>
                                                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
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
                                        [#if module.isTeachingContentSupport?? && module.isTeachingContentSupport]
                                            <w:p w:rsidR="00CF1355" w:rsidRDefault="00CF1355" w:rsidP="00CF1355">
                                                <w:pPr>
                                                    <w:tabs>
                                                        <w:tab w:val="left" w:pos="4680"/>
                                                        <w:tab w:val="left" w:pos="4860"/>
                                                    </w:tabs>
                                                    <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                    <w:rPr>
                                                        <w:rFonts w:hint="eastAsia"/>
                                                        <w:i/>
                                                        <w:color w:val="0000FF"/>
                                                        <w:szCs w:val="20"/>
                                                    </w:rPr>
                                                </w:pPr>
                                                <w:r>
                                                    <w:rPr>
                                                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                        <w:color w:val="000000"/>
                                                    </w:rPr>
                                                    <w:t>重点支持课程目标：[#if teachingContent.indications??][#list teachingContent.indications as indication]CO${indication.sort} ${indication.content}[#sep]，[/#list][/#if]</w:t>
                                                </w:r>
                                            </w:p>
                                        [/#if]
                                    [/#list]
                                [/#if]
                                <!-- 次要内容列表 -->
                                [#if module.secondaryContents??]
                                    [#list module.secondaryContents as secondaryContent]
                                        [#if secondaryContent.title??]
                                            <w:p w:rsidR="00790A75" w:rsidRDefault="00790A75" w:rsidP="00CF1355">
                                                <w:pPr>
                                                    <w:tabs>
                                                        <w:tab w:val="left" w:pos="4680"/>
                                                        <w:tab w:val="left" w:pos="4860"/>
                                                    </w:tabs>
                                                    <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                    <w:rPr>
                                                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                    </w:rPr>
                                                </w:pPr>
                                            </w:p>
                                            <w:p w:rsidR="00635407" w:rsidRPr="00790A75" w:rsidRDefault="00790A75" w:rsidP="00790A75">
                                                <w:pPr>
                                                    <w:adjustRightInd w:val="0"/>
                                                    <w:snapToGrid w:val="0"/>
                                                    <w:spacing w:line="360" w:lineRule="auto"/>
                                                    <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                    <w:rPr>
                                                        <w:rFonts w:ascii="宋体" w:cs="宋体"/>
                                                        <w:kern w:val="0"/>
                                                    </w:rPr>
                                                </w:pPr>
                                                <w:r w:rsidRPr="006D4A7F">
                                                    <w:rPr>
                                                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                                        <w:kern w:val="0"/>
                                                    </w:rPr>
                                                    <w:t>${secondaryContent.title}</w:t>
                                                </w:r>
                                            </w:p>
                                        [/#if]
                                        [#if secondaryContent.secondaryContent??]
                                            [#if secondaryContent.secondaryContent?is_string && secondaryContent.secondaryContent?index_of("<w:") != -1 && secondaryContent.secondaryContent?index_of("</w:") != -1]
                                                ${secondaryContent.secondaryContent}
                                            [#else]
                                                [#assign mainContentMap = secondaryContent.secondaryContent]
                                                [#if mainContentMap?is_hash]
                                                    [#if mainContentMap.isNonChinese == 0]
                                                        <w:p w:rsidR="00CF1355" w:rsidRPr="00F96D02" w:rsidRDefault="00CF1355" w:rsidP="00CF1355">
                                                            <w:pPr>
                                                                <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                                <w:rPr>
                                                                    <w:color w:val="000000"/>
                                                                </w:rPr>
                                                            </w:pPr>
                                                            <w:r>
                                                                <w:rPr>
                                                                    <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                </w:rPr>
                                                                <w:t></w:t>
                                                            </w:r>
                                                        </w:p>
                                                    [#else]
                                                        [#assign paragraphDataList = mainContentMap.paragraphDataList]
                                                        [#if paragraphDataList?? && paragraphDataList?size >0]
                                                            [#list paragraphDataList as paragraph]
                                                                <w:p w:rsidR="00CF1355" w:rsidRPr="00F96D02" w:rsidRDefault="00CF1355" w:rsidP="00CF1355">
                                                                    <w:pPr>
                                                                        <w:pStyle w:val="ad"/>
                                                                        <w:spacing w:line="300" w:lineRule="auto"/>
                                                                        <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                                        <w:rPr>
                                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                                        </w:rPr>
                                                                    </w:pPr>
                                                                    [#if paragraph?? && paragraph?size > 0]
                                                                        [#list paragraph as sentence]
                                                                            [#if sentence?? && sentence.isNonChinese??]
                                                                                [#if sentence.isNonChinese == 1]
                                                                                    <w:r w:rsidRPr="00833795">
                                                                                        <w:rPr>
                                                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                                                                                        </w:rPr>
                                                                                        <w:t>[#if sentence.content??]${sentence.content}[/#if]</w:t>
                                                                                    </w:r>
                                                                                [#else ]
                                                                                    <w:r>
                                                                                        <w:rPr>
                                                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
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
                                        [#if module.isSecondaryContentSupport?? && module.isSecondaryContentSupport]
                                            <w:p w:rsidR="00CF1355" w:rsidRDefault="00CF1355" w:rsidP="00CF1355">
                                                <w:pPr>
                                                    <w:tabs>
                                                        <w:tab w:val="left" w:pos="4680"/>
                                                        <w:tab w:val="left" w:pos="4860"/>
                                                    </w:tabs>
                                                    <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                    <w:rPr>
                                                        <w:rFonts w:hint="eastAsia"/>
                                                        <w:i/>
                                                        <w:color w:val="0000FF"/>
                                                        <w:szCs w:val="20"/>
                                                    </w:rPr>
                                                </w:pPr>
                                                <w:r>
                                                    <w:rPr>
                                                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                        <w:color w:val="000000"/>
                                                    </w:rPr>
                                                    <w:t>重点支持课程目标：[#if secondaryContent.indications??][#list secondaryContent.indications as indication]CO${indication.sort} ${indication.content}[#sep]，[/#list][/#if]</w:t>
                                                </w:r>
                                            </w:p>
                                        [/#if]
                                    [/#list]
                                [/#if]
                                [#if module.tables??]
                                    [#--每个模块的表格合并初始化--]
                                    [#assign merge = 0]
                                    [#list module.tables as table]
                                        [#if table.tableTopDetail??]
                                        [#--富文本以下形式，复制即可--]
                                            [#if table.tableTopDetail?is_string && table.tableTopDetail?index_of("<w:") != -1 && table.tableTopDetail?index_of("</w:") != -1]
                                                ${table.tableTopDetail}
                                            [#else ]
                                                <w:p w:rsidR="00790A75" w:rsidRDefault="00790A75" w:rsidP="00CF1355">
                                                    <w:pPr>
                                                        <w:tabs>
                                                            <w:tab w:val="left" w:pos="4680"/>
                                                            <w:tab w:val="left" w:pos="4860"/>
                                                        </w:tabs>
                                                        <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:hint="eastAsia"/>
                                                        </w:rPr>
                                                    </w:pPr>
                                                </w:p>
                                                <w:p w:rsidR="00635407" w:rsidRPr="00790A75" w:rsidRDefault="00790A75" w:rsidP="00790A75">
                                                    <w:pPr>
                                                        <w:adjustRightInd w:val="0"/>
                                                        <w:snapToGrid w:val="0"/>
                                                        <w:spacing w:line="360" w:lineRule="auto"/>
                                                        <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:cs="宋体"/>
                                                            <w:kern w:val="0"/>
                                                        </w:rPr>
                                                    </w:pPr>
                                                    <w:r w:rsidRPr="006D4A7F">
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                                            <w:kern w:val="0"/>
                                                        </w:rPr>
                                                        <w:t>${table.tableTopDetail}</w:t>
                                                    </w:r>
                                                </w:p>

                                            [/#if]

                                        [/#if]

                                        [#if table.tableName??]
                                            <w:p w:rsidR="00635407" w:rsidRDefault="00635407" w:rsidP="00635407">
                                                <w:pPr>
                                                    <w:jc w:val="center"/>
                                                    <w:rPr>
                                                        <w:rFonts w:hint="eastAsia"/>
                                                        <w:b/>
                                                    </w:rPr>
                                                </w:pPr>
                                                <w:r>
                                                    <w:rPr>
                                                        <w:rFonts w:hint="eastAsia"/>
                                                        <w:b/>
                                                    </w:rPr>
                                                    <w:t>${table.tableName}</w:t>
                                                </w:r>
                                            </w:p>
                                        [/#if]
                                        <w:tbl>
                                            <w:tblPr>
                                                <w:tblW w:w="8365" w:type="dxa"/>
                                                <w:jc w:val="center"/>
                                                <w:tblBorders>
                                                    <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                    <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                    <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                    <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                    <w:insideH w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                    <w:insideV w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                                                </w:tblBorders>
                                                <w:tblLook w:val="04A0" w:firstRow="1" w:lastRow="0" w:firstColumn="1" w:lastColumn="0" w:noHBand="0" w:noVBand="1"/>
                                            </w:tblPr>
                                            <w:tblGrid>

                                            </w:tblGrid>
                                            [#if table.headers??]
                                                [#assign merge = 0]
                                                [#--判断表头是否存在课时--]
                                                [#list table.headers as header]
                                                    [#--合并多少列--]
                                                    [#if header.hoursType??]
                                                        [#assign merge += 1]

                                                    [/#if]
                                                [/#list]
                                                [#--新增一行空白行--]
                                                [#if merge>0]
                                                    <w:tr w:rsidR="00CF1355" w:rsidRPr="00EE3B93" w:rsidTr="0073036F">
                                                        <w:tc>
                                                            <w:tcPr>
                                                                <w:tcW w:w="730" w:type="dxa"/>

                                                                    <w:vMerge w:val="restart"/>

                                                                <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                <w:vAlign w:val="center"/>
                                                            </w:tcPr>
                                                            <w:p w:rsidR="00CF1355" w:rsidRPr="005821DE" w:rsidRDefault="00CF1355" w:rsidP="0073036F">
                                                                <w:pPr>
                                                                    <w:tabs>
                                                                        <w:tab w:val="left" w:pos="4680"/>
                                                                        <w:tab w:val="left" w:pos="4860"/>
                                                                    </w:tabs>
                                                                    <w:jc w:val="center"/>
                                                                    <w:rPr>
                                                                        <w:sz w:val="18"/>
                                                                        <w:szCs w:val="18"/>
                                                                    </w:rPr>
                                                                </w:pPr>
                                                                <w:r w:rsidRPr="005821DE">
                                                                    <w:rPr>
                                                                        <w:rFonts w:hint="eastAsia"/>
                                                                        <w:sz w:val="18"/>
                                                                        <w:szCs w:val="18"/>
                                                                    </w:rPr>
                                                                    <w:t>序号</w:t>
                                                                </w:r>
                                                            </w:p>
                                                        </w:tc>

                                                        [#assign show = 0]
                                                        [#list table.headers as header]

                                                            [#--小计是否显示设置，2、3说明表头是数字类型--]
                                                            [#if header.type == 2 || header.type == 3 ]
                                                                [#assign show = 1]
                                                            [/#if]
                                                            [#--判断是否需要合并 merge>0有合并行,合并之后设置为-1,之后只能header.hoursType为null的近去--]
                                                            [#if merge >0 || !header.hoursType??]

                                                                <w:tc>
                                                                    <w:tcPr>
                                                                        <w:tcW w:w="0" w:type="auto"/>
                                                                        [#--判断是行合并还是列合并--]
                                                                        [#if header.hoursType??]
                                                                            [#if merge !=0 && merge !=-1]
                                                                                [#--列合并--]
                                                                                 <w:gridSpan w:val="${merge}"/>
                                                                                [#assign merge = -1]
                                                                            [/#if]
                                                                        [#else ]
                                                                            [#--行合并--]
                                                                            <w:vMerge w:val="restart"/>
                                                                        [/#if]

                                                                        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                        <w:vAlign w:val="center"/>
                                                                    </w:tcPr>
                                                                    <w:p w:rsidR="00CF1355" w:rsidRPr="005821DE" w:rsidRDefault="00CF1355" w:rsidP="0073036F">
                                                                        <w:pPr>
                                                                            <w:tabs>
                                                                                <w:tab w:val="left" w:pos="4680"/>
                                                                                <w:tab w:val="left" w:pos="4860"/>
                                                                            </w:tabs>
                                                                            <w:jc w:val="center"/>
                                                                            <w:rPr>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                        </w:pPr>
                                                                        <w:r w:rsidRPr="005821DE">
                                                                            <w:rPr>
                                                                                <w:rFonts w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                            <w:t>
                                                                                [#if  header.hoursType??]
                                                                                    课内学时
                                                                                  [#else ]
                                                                                      [#if header.name??] ${header.name}[/#if]
                                                                                [/#if]
                                                                            </w:t>
                                                                        </w:r>
                                                                    </w:p>
                                                                </w:tc>
                                                            [/#if]
                                                        [/#list]
                                                    </w:tr>
                                                [#--再次循环课时,增加一行合并为课内学时--]

                                                    <w:tr w:rsidR="00CF1355" w:rsidRPr="00EE3B93" w:rsidTr="0073036F">
                                                        <w:tc>
                                                            <w:tcPr>
                                                                <w:tcW w:w="730" w:type="dxa"/>
                                                                [#--上下两行合并结束属性--]
                                                                      <w:vMerge/>
                                                                <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                <w:vAlign w:val="center"/>
                                                            </w:tcPr>
                                                            <w:p w:rsidR="00CF1355" w:rsidRPr="005821DE" w:rsidRDefault="00CF1355" w:rsidP="0073036F">
                                                                <w:pPr>
                                                                    <w:tabs>
                                                                        <w:tab w:val="left" w:pos="4680"/>
                                                                        <w:tab w:val="left" w:pos="4860"/>
                                                                    </w:tabs>
                                                                    <w:jc w:val="center"/>
                                                                    <w:rPr>
                                                                        <w:sz w:val="18"/>
                                                                        <w:szCs w:val="18"/>
                                                                    </w:rPr>
                                                                </w:pPr>
                                                                <w:r w:rsidRPr="005821DE">
                                                                    <w:rPr>
                                                                        <w:rFonts w:hint="eastAsia"/>
                                                                        <w:sz w:val="18"/>
                                                                        <w:szCs w:val="18"/>
                                                                    </w:rPr>
                                                                   [#-- <w:t></w:t>--]
                                                                </w:r>
                                                            </w:p>
                                                        </w:tc>

                                                        [#list table.headers as header]



                                                            <w:tc>
                                                                <w:tcPr>
                                                                    <w:tcW w:w="0" w:type="auto"/>
                                                                    [#if header.hoursType??]

                                                                    [#else ]
                                                                        <w:vMerge/>
                                                                    [/#if]

                                                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                    <w:vAlign w:val="center"/>
                                                                </w:tcPr>
                                                                <w:p w:rsidR="00CF1355" w:rsidRPr="005821DE" w:rsidRDefault="00CF1355" w:rsidP="0073036F">
                                                                    <w:pPr>
                                                                        <w:tabs>
                                                                            <w:tab w:val="left" w:pos="4680"/>
                                                                            <w:tab w:val="left" w:pos="4860"/>
                                                                        </w:tabs>
                                                                        <w:jc w:val="center"/>
                                                                        <w:rPr>
                                                                            <w:sz w:val="18"/>
                                                                            <w:szCs w:val="18"/>
                                                                        </w:rPr>
                                                                    </w:pPr>
                                                                    <w:r w:rsidRPr="005821DE">
                                                                        <w:rPr>
                                                                            <w:rFonts w:hint="eastAsia"/>
                                                                            <w:sz w:val="18"/>
                                                                            <w:szCs w:val="18"/>
                                                                        </w:rPr>

                                                                            [#if header.hoursType??]
                                                                             <w:t>
                                                                                [#if header.name??] ${header.name}[/#if]
                                                                             </w:t>
                                                                            [/#if]

                                                                    </w:r>
                                                                </w:p>
                                                            </w:tc>
                                                        [/#list]
                                                    </w:tr>
                                                [#else ]
                                                    <w:tr w:rsidR="00CF1355" w:rsidRPr="00EE3B93" w:rsidTr="0073036F">
                                                        <w:tc>
                                                            <w:tcPr>
                                                                <w:tcW w:w="730" w:type="dxa"/>
                                                                <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                <w:vAlign w:val="center"/>
                                                            </w:tcPr>
                                                            <w:p w:rsidR="00CF1355" w:rsidRPr="005821DE" w:rsidRDefault="00CF1355" w:rsidP="0073036F">
                                                                <w:pPr>
                                                                    <w:tabs>
                                                                        <w:tab w:val="left" w:pos="4680"/>
                                                                        <w:tab w:val="left" w:pos="4860"/>
                                                                    </w:tabs>
                                                                    <w:jc w:val="center"/>
                                                                    <w:rPr>
                                                                        <w:sz w:val="18"/>
                                                                        <w:szCs w:val="18"/>
                                                                    </w:rPr>
                                                                </w:pPr>
                                                                <w:r w:rsidRPr="005821DE">
                                                                    <w:rPr>
                                                                        <w:rFonts w:hint="eastAsia"/>
                                                                        <w:sz w:val="18"/>
                                                                        <w:szCs w:val="18"/>
                                                                    </w:rPr>
                                                                    <w:t>序号</w:t>
                                                                </w:r>
                                                            </w:p>
                                                        </w:tc>

                                                        [#assign show = 0]
                                                        [#list table.headers as header]

                                                        [#--小计是否显示设置，2、3说明表头是数字类型--]
                                                            [#if header.type == 2 || header.type == 3 ]
                                                                [#assign show = 1]
                                                            [/#if]


                                                            <w:tc>
                                                                <w:tcPr>
                                                                    <w:tcW w:w="0" w:type="auto"/>
                                                                    <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                    <w:vAlign w:val="center"/>
                                                                </w:tcPr>
                                                                <w:p w:rsidR="00CF1355" w:rsidRPr="005821DE" w:rsidRDefault="00CF1355" w:rsidP="0073036F">
                                                                    <w:pPr>
                                                                        <w:tabs>
                                                                            <w:tab w:val="left" w:pos="4680"/>
                                                                            <w:tab w:val="left" w:pos="4860"/>
                                                                        </w:tabs>
                                                                        <w:jc w:val="center"/>
                                                                        <w:rPr>
                                                                            <w:sz w:val="18"/>
                                                                            <w:szCs w:val="18"/>
                                                                        </w:rPr>
                                                                    </w:pPr>
                                                                    <w:r w:rsidRPr="005821DE">
                                                                        <w:rPr>
                                                                            <w:rFonts w:hint="eastAsia"/>
                                                                            <w:sz w:val="18"/>
                                                                            <w:szCs w:val="18"/>
                                                                        </w:rPr>
                                                                        <w:t>[#if header.name??] ${header.name}[/#if]</w:t>
                                                                    </w:r>
                                                                </w:p>
                                                            </w:tc>
                                                        [/#list]
                                                    </w:tr>
                                                [/#if]
                                            [/#if]
                                            [#if table.tableDetails??]
                                                [#if table.headers?? && table.headers?size>0]
                                                    [#assign totalRowNum = table.tableDetails?size / table.headers?size]
                                                    [#assign tableDetailIndex = 1]
                                                [/#if]
                                                [#list 1..totalRowNum as currentRowNum]
                                                    <w:tr w:rsidR="00CF1355" w:rsidRPr="00EE3B93" w:rsidTr="0073036F">
                                                        <w:tc>
                                                            <w:tcPr>
                                                                <w:tcW w:w="730" w:type="dxa"/>
                                                                <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                <w:vAlign w:val="center"/>
                                                            </w:tcPr>
                                                            <w:p w:rsidR="00CF1355" w:rsidRPr="005821DE" w:rsidRDefault="00CF1355" w:rsidP="0073036F">
                                                                <w:pPr>
                                                                    <w:tabs>
                                                                        <w:tab w:val="left" w:pos="4680"/>
                                                                        <w:tab w:val="left" w:pos="4860"/>
                                                                    </w:tabs>
                                                                    <w:jc w:val="center"/>
                                                                    <w:rPr>
                                                                        <w:sz w:val="18"/>
                                                                        <w:szCs w:val="18"/>
                                                                    </w:rPr>
                                                                </w:pPr>
                                                                <w:r w:rsidRPr="005821DE">
                                                                    <w:rPr>
                                                                        <w:rFonts w:hint="eastAsia"/>
                                                                        <w:sz w:val="18"/>
                                                                        <w:szCs w:val="18"/>
                                                                    </w:rPr>
                                                                    <w:t>
                                                                        ${currentRowNum}
                                                                    </w:t>
                                                                </w:r>
                                                            </w:p>
                                                        </w:tc>
                                                        [#list table.tableDetails as tableDetail]
                                                            [#if tableDetail.rowOrdinal == currentRowNum]
                                                                <w:tc>
                                                                    <w:tcPr>
                                                                        <w:tcW w:w="0" w:type="auto"/>
                                                                        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                                        <w:vAlign w:val="center"/>
                                                                    </w:tcPr>
                                                                    <w:p w:rsidR="00CF1355" w:rsidRPr="005821DE" w:rsidRDefault="00CF1355" w:rsidP="0073036F">
                                                                        <w:pPr>
                                                                            <w:tabs>
                                                                                <w:tab w:val="left" w:pos="4680"/>
                                                                                <w:tab w:val="left" w:pos="4860"/>
                                                                            </w:tabs>
                                                                            <w:jc w:val="center"/>
                                                                            <w:rPr>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                        </w:pPr>
                                                                        <w:r w:rsidRPr="005821DE">
                                                                            <w:rPr>
                                                                                <w:rFonts w:hint="eastAsia"/>
                                                                                <w:sz w:val="18"/>
                                                                                <w:szCs w:val="18"/>
                                                                            </w:rPr>
                                                                            <w:t>
                                                                                [#if tableDetail.type == INDICATION]
                                                                                    [#if tableDetail.indications?? && tableDetail.indications?size>0]
                                                                                        课程目标：[#list tableDetail.indications as indication]CO${indication.sort} ${indication.content}[#sep]、[/#list]
                                                                                    [/#if]

                                                                                [#elseif tableDetail.type == INDICTIONPORT]
                                                                                    [#if tableDetail.indications?? && tableDetail.indications?size>0]
                                                                                        指标点：[#list tableDetail.indications as indication] ${indication.content}[#sep]、[/#list]
                                                                                    [/#if]
                                                                                [#else]
                                                                                    [#if tableDetail.content??]
                                                                                        ${tableDetail.content}
                                                                                    [/#if]
                                                                                [/#if]
                                                                            </w:t>
                                                                        </w:r>
                                                                    </w:p>
                                                                </w:tc>
                                                            [/#if]

                                                        [/#list]
                                                    </w:tr>
                                                [/#list]



                                                [#if show == 1 ]

                                                <w:tr w:rsidR="00CF1355" w:rsidRPr="00EE3B93" w:rsidTr="0073036F">
                                                <w:tc>
                                                    <w:tcPr>
                                                        <w:tcW w:w="730" w:type="dxa"/>
                                                        <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                        <w:vAlign w:val="center"/>
                                                    </w:tcPr>
                                                    <w:p w:rsidR="00CF1355" w:rsidRPr="005821DE" w:rsidRDefault="00CF1355" w:rsidP="0073036F">
                                                        <w:pPr>
                                                            <w:tabs>
                                                                <w:tab w:val="left" w:pos="4680"/>
                                                                <w:tab w:val="left" w:pos="4860"/>
                                                            </w:tabs>
                                                            <w:jc w:val="center"/>
                                                            <w:rPr>
                                                                <w:sz w:val="18"/>
                                                                <w:szCs w:val="18"/>
                                                            </w:rPr>
                                                        </w:pPr>
                                                        <w:r w:rsidRPr="005821DE">
                                                            <w:rPr>
                                                                <w:rFonts w:hint="eastAsia"/>
                                                                <w:sz w:val="18"/>
                                                                <w:szCs w:val="18"/>
                                                            </w:rPr>
                                                            <w:t>
                                                                小计
                                                            </w:t>
                                                        </w:r>
                                                    </w:p>
                                                </w:tc>
                                                [#list table.headers as header]
                                                    <w:tc>
                                                        <w:tcPr>
                                                            <w:tcW w:w="0" w:type="auto"/>
                                                            <w:shd w:val="clear" w:color="auto" w:fill="auto"/>
                                                            <w:vAlign w:val="center"/>
                                                        </w:tcPr>
                                                        <w:p w:rsidR="00CF1355" w:rsidRPr="005821DE" w:rsidRDefault="00CF1355" w:rsidP="0073036F">
                                                            <w:pPr>
                                                                <w:tabs>
                                                                    <w:tab w:val="left" w:pos="4680"/>
                                                                    <w:tab w:val="left" w:pos="4860"/>
                                                                </w:tabs>
                                                                <w:jc w:val="center"/>
                                                                <w:rPr>
                                                                    <w:sz w:val="18"/>
                                                                    <w:szCs w:val="18"/>
                                                                </w:rPr>
                                                            </w:pPr>
                                                            <w:r w:rsidRPr="005821DE">
                                                                <w:rPr>
                                                                    <w:rFonts w:hint="eastAsia"/>
                                                                    <w:sz w:val="18"/>
                                                                    <w:szCs w:val="18"/>
                                                                </w:rPr>
                                                                <w:t>
                                                                    [#assign totalTemp = 0]
                                                                    [#if header.type?? && (header.type == NUMBER || header.type == TOTAL) ]
                                                                        [#if header.hours??]
                                                                            ${header.hours}
                                                                        [#else]
                                                                            [#list table.tableDetails as tableDetail]
                                                                                [#if (tableDetail.columnOrdinal == header.columnOrdinal) && tableDetail.content??]
                                                                                    [#assign totalTemp = (totalTemp + tableDetail.content?number)]
                                                                                [/#if]
                                                                            [/#list]
                                                                            ${totalTemp}
                                                                        [/#if]
                                                                    [#else]
                                                                    [/#if]
                                                                </w:t>
                                                            </w:r>
                                                        </w:p>
                                                    </w:tc>
                                                [/#list]
                                            </w:tr>

                                                    [/#if]


                                            [/#if]

                                        </w:tbl>
                                        [#if table.tableBottomDetail??]
                                            <w:p w:rsidR="00CA327E" w:rsidRPr="00F743C8" w:rsidRDefault="00CA327E" w:rsidP="00CA327E">
                                                <w:pPr>
                                                    <w:pStyle w:val="a5"/>
                                                    <w:ind w:firstLine="400"/>
                                                    <w:rPr>
                                                        <w:rFonts w:hint="eastAsia"/>
                                                        <w:szCs w:val="21"/>
                                                        <w:lang w:val="en-US"/>
                                                    </w:rPr>
                                                </w:pPr>
                                            </w:p>
                                            [#if table.tableBottomDetail?is_string && table.tableBottomDetail?index_of("<w:") != -1 && table.tableBottomDetail?index_of("</w:") != -1]
                                                ${table.tableBottomDetail}
                                            [#else]


                                                <w:p w:rsidR="00635407" w:rsidRPr="00790A75" w:rsidRDefault="00790A75" w:rsidP="00790A75">
                                                    <w:pPr>
                                                        <w:adjustRightInd w:val="0"/>
                                                        <w:snapToGrid w:val="0"/>
                                                        <w:spacing w:line="360" w:lineRule="auto"/>
                                                        <w:ind w:firstLineChars="200" w:firstLine="420"/>
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:cs="宋体"/>
                                                            <w:kern w:val="0"/>
                                                        </w:rPr>
                                                    </w:pPr>
                                                    <w:r w:rsidRPr="006D4A7F">
                                                        <w:rPr>
                                                            <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="宋体" w:hint="eastAsia"/>
                                                            <w:kern w:val="0"/>
                                                        </w:rPr>
                                                        <w:t>${table.tableBottomDetail}</w:t>
                                                    </w:r>
                                                </w:p>

                                            [/#if]
                                        [#--换行--]

                                        [/#if]
                                    [/#list]
                                [/#if]
                                [#sep]

                            [/#list]
                        [/#if]
                        <w:sectPr w:rsidR="00CA327E" w:rsidRPr="00CF1355" w:rsidSect="00394288">
                            <w:footerReference w:type="default" r:id="rId7"/>
                            <w:pgSz w:w="11906" w:h="16838"/>
                            <w:pgMar w:top="1440" w:right="1800" w:bottom="1440" w:left="1800" w:header="851" w:footer="992" w:gutter="0"/>
                            <w:cols w:space="425"/>
                            <w:docGrid w:type="lines" w:linePitch="312"/>
                        </w:sectPr>
                    [/#if]
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
                    <w:p w:rsidR="00396808" w:rsidRDefault="00396808" w:rsidP="00287A0B">
                        <w:pPr>
                            <w:spacing w:line="240" w:lineRule="auto"/>
                        </w:pPr>
                        <w:r>
                            <w:separator/>
                        </w:r>
                    </w:p>
                </w:footnote>
                <w:footnote w:type="continuationSeparator" w:id="0">
                    <w:p w:rsidR="00396808" w:rsidRDefault="00396808" w:rsidP="00287A0B">
                        <w:pPr>
                            <w:spacing w:line="240" w:lineRule="auto"/>
                        </w:pPr>
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
                    <w:p w:rsidR="00396808" w:rsidRDefault="00396808" w:rsidP="00287A0B">
                        <w:pPr>
                            <w:spacing w:line="240" w:lineRule="auto"/>
                        </w:pPr>
                        <w:r>
                            <w:separator/>
                        </w:r>
                    </w:p>
                </w:endnote>
                <w:endnote w:type="continuationSeparator" w:id="0">
                    <w:p w:rsidR="00396808" w:rsidRDefault="00396808" w:rsidP="00287A0B">
                        <w:pPr>
                            <w:spacing w:line="240" w:lineRule="auto"/>
                        </w:pPr>
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
                <w:p w:rsidR="00396FB3" w:rsidRDefault="00396FB3">
                    <w:pPr>
                        <w:pStyle w:val="a7"/>
                        <w:jc w:val="center"/>
                    </w:pPr>
                    <w:r>
                        <w:fldChar w:fldCharType="begin"/>
                    </w:r>
                    <w:r>
                        <w:instrText xml:space="preserve"> PAGE   \* MERGEFORMAT </w:instrText>
                    </w:r>
                    <w:r>
                        <w:fldChar w:fldCharType="separate"/>
                    </w:r>
                    <w:r w:rsidR="00AD751D" w:rsidRPr="00AD751D">
                        <w:rPr>
                            <w:noProof/>
                            <w:lang w:val="zh-CN"/>
                        </w:rPr>
                        <w:t>3</w:t>
                    </w:r>
                    <w:r>
                        <w:fldChar w:fldCharType="end"/>
                    </w:r>
                </w:p>
                <w:p w:rsidR="00396FB3" w:rsidRDefault="00396FB3">
                    <w:pPr>
                        <w:pStyle w:val="a7"/>
                    </w:pPr>
                </w:p>
            </w:ftr>
        </pkg:xmlData>
    </pkg:part>
    <pkg:part pkg:name="/word/theme/theme1.xml" pkg:contentType="application/vnd.openxmlformats-officedocument.theme+xml">
        <pkg:xmlData>
            <a:theme
                    xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" name="Office 主题​​">
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
                            <a:latin typeface="等线 Light" panose="020F0302020204030204"/>
                            <a:ea typeface=""/>
                            <a:cs typeface=""/>
                            <a:font script="Jpan" typeface="游ゴシック Light"/>
                            <a:font script="Hang" typeface="맑은 고딕"/>
                            <a:font script="Hans" typeface="等线 Light"/>
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
                            <a:latin typeface="等线" panose="020F0502020204030204"/>
                            <a:ea typeface=""/>
                            <a:cs typeface=""/>
                            <a:font script="Jpan" typeface="游明朝"/>
                            <a:font script="Hang" typeface="맑은 고딕"/>
                            <a:font script="Hans" typeface="等线"/>
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
                <w:zoom w:percent="110"/>
                <w:bordersDoNotSurroundHeader/>
                <w:bordersDoNotSurroundFooter/>
                <w:doNotTrackMoves/>
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
                    <w:useNormalStyleForList/>
                    <w:doNotUseIndentAsNumberingTabStop/>
                    <w:useAltKinsokuLineBreakRules/>
                    <w:allowSpaceOfSameStyleInTable/>
                    <w:doNotSuppressIndentation/>
                    <w:doNotAutofitConstrainedTables/>
                    <w:autofitToFirstFixedWidthCell/>
                    <w:displayHangulFixedWidth/>
                    <w:splitPgBreakAndParaMark/>
                    <w:doNotVertAlignCellWithSp/>
                    <w:doNotBreakConstrainedForcedTable/>
                    <w:doNotVertAlignInTxbx/>
                    <w:useAnsiKerningPairs/>
                    <w:cachedColBalance/>
                    <w:compatSetting w:name="compatibilityMode" w:uri="http://schemas.microsoft.com/office/word" w:val="11"/>
                </w:compat>
                <w:rsids>
                    <w:rsidRoot w:val="00E60125"/>
                    <w:rsid w:val="000065E8"/>
                    <w:rsid w:val="000406BE"/>
                    <w:rsid w:val="00045837"/>
                    <w:rsid w:val="00081E62"/>
                    <w:rsid w:val="000A3AF4"/>
                    <w:rsid w:val="000C62FB"/>
                    <w:rsid w:val="00111B7F"/>
                    <w:rsid w:val="0017121C"/>
                    <w:rsid w:val="00172DC0"/>
                    <w:rsid w:val="001A3DB9"/>
                    <w:rsid w:val="001B7FD6"/>
                    <w:rsid w:val="00200C0D"/>
                    <w:rsid w:val="002364E3"/>
                    <w:rsid w:val="00282265"/>
                    <w:rsid w:val="00287A0B"/>
                    <w:rsid w:val="002B3A0D"/>
                    <w:rsid w:val="002E5D6B"/>
                    <w:rsid w:val="002F0F66"/>
                    <w:rsid w:val="003219BF"/>
                    <w:rsid w:val="00336284"/>
                    <w:rsid w:val="00394288"/>
                    <w:rsid w:val="00396808"/>
                    <w:rsid w:val="00396FB3"/>
                    <w:rsid w:val="003A6CBB"/>
                    <w:rsid w:val="003B55EB"/>
                    <w:rsid w:val="003B6951"/>
                    <w:rsid w:val="003C296F"/>
                    <w:rsid w:val="003E3F00"/>
                    <w:rsid w:val="00404292"/>
                    <w:rsid w:val="004154E7"/>
                    <w:rsid w:val="00567F40"/>
                    <w:rsid w:val="005A78F1"/>
                    <w:rsid w:val="005D54D7"/>
                    <w:rsid w:val="0062662C"/>
                    <w:rsid w:val="00635407"/>
                    <w:rsid w:val="006842CB"/>
                    <w:rsid w:val="00692F00"/>
                    <w:rsid w:val="006C6586"/>
                    <w:rsid w:val="006D101D"/>
                    <w:rsid w:val="006D2ABB"/>
                    <w:rsid w:val="00724756"/>
                    <w:rsid w:val="0073036F"/>
                    <w:rsid w:val="00790A75"/>
                    <w:rsid w:val="007A3BE1"/>
                    <w:rsid w:val="007B7700"/>
                    <w:rsid w:val="007C38D8"/>
                    <w:rsid w:val="007E5F34"/>
                    <w:rsid w:val="0080401B"/>
                    <w:rsid w:val="008221A7"/>
                    <w:rsid w:val="008445C6"/>
                    <w:rsid w:val="008603EA"/>
                    <w:rsid w:val="008D4C72"/>
                    <w:rsid w:val="00935C42"/>
                    <w:rsid w:val="009B03C1"/>
                    <w:rsid w:val="00A0351B"/>
                    <w:rsid w:val="00A22C7E"/>
                    <w:rsid w:val="00A41EE3"/>
                    <w:rsid w:val="00AD2D8C"/>
                    <w:rsid w:val="00AD2E02"/>
                    <w:rsid w:val="00AD751D"/>
                    <w:rsid w:val="00AD7C37"/>
                    <w:rsid w:val="00B011EF"/>
                    <w:rsid w:val="00B14607"/>
                    <w:rsid w:val="00B770D0"/>
                    <w:rsid w:val="00B8427D"/>
                    <w:rsid w:val="00BD08FD"/>
                    <w:rsid w:val="00BF0F1B"/>
                    <w:rsid w:val="00C411E8"/>
                    <w:rsid w:val="00C41DD0"/>
                    <w:rsid w:val="00C440E4"/>
                    <w:rsid w:val="00C6233E"/>
                    <w:rsid w:val="00C65B26"/>
                    <w:rsid w:val="00CA06A9"/>
                    <w:rsid w:val="00CA327E"/>
                    <w:rsid w:val="00CF1355"/>
                    <w:rsid w:val="00D24D55"/>
                    <w:rsid w:val="00D64118"/>
                    <w:rsid w:val="00E010F3"/>
                    <w:rsid w:val="00E10A60"/>
                    <w:rsid w:val="00E2214B"/>
                    <w:rsid w:val="00E36821"/>
                    <w:rsid w:val="00E45A3E"/>
                    <w:rsid w:val="00E575EA"/>
                    <w:rsid w:val="00E60125"/>
                    <w:rsid w:val="00E66879"/>
                    <w:rsid w:val="00EB0056"/>
                    <w:rsid w:val="00EB4C56"/>
                    <w:rsid w:val="00EB53C3"/>
                    <w:rsid w:val="00ED42A7"/>
                    <w:rsid w:val="00ED5339"/>
                    <w:rsid w:val="00F337AF"/>
                    <w:rsid w:val="00F65D6F"/>
                    <w:rsid w:val="00F743C8"/>
                    <w:rsid w:val="00F7641D"/>
                    <w:rsid w:val="00FA35C6"/>
                    <w:rsid w:val="00FB1F9C"/>
                    <w:rsid w:val="00FB635F"/>
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
                <w15:chartTrackingRefBased/>
                <w15:docId w15:val="{A00AB709-3C1E-4F97-9891-ED8F1A0C6778}"/>
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
                <w:optimizeForBrowser/>
                <w:targetScreenSz w:val="800x600"/>
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
                            <w:rFonts w:ascii="Calibri" w:eastAsia="宋体" w:hAnsi="Calibri" w:cs="Times New Roman"/>
                            <w:lang w:val="en-US" w:eastAsia="zh-CN" w:bidi="ar-SA"/>
                        </w:rPr>
                    </w:rPrDefault>
                    <w:pPrDefault/>
                </w:docDefaults>
                <w:latentStyles w:defLockedState="0" w:defUIPriority="99" w:defSemiHidden="0" w:defUnhideWhenUsed="0" w:defQFormat="0" w:count="371">
                    <w:lsdException w:name="Normal" w:uiPriority="0" w:qFormat="1"/>
                    <w:lsdException w:name="heading 1" w:uiPriority="9" w:qFormat="1"/>
                    <w:lsdException w:name="heading 2" w:semiHidden="1" w:uiPriority="9" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 3" w:semiHidden="1" w:uiPriority="9" w:unhideWhenUsed="1" w:qFormat="1"/>
                    <w:lsdException w:name="heading 4" w:semiHidden="1" w:uiPriority="9" w:unhideWhenUsed="1" w:qFormat="1"/>
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
                    <w:lsdException w:name="Body Text" w:semiHidden="1" w:unhideWhenUsed="1"/>
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
                    <w:lsdException w:name="Plain Text" w:semiHidden="1" w:uiPriority="0" w:unhideWhenUsed="1"/>
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
                    <w:lsdException w:name="Table Grid" w:uiPriority="59"/>
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
                    <w:lsdException w:name="List Paragraph" w:uiPriority="0" w:qFormat="1"/>
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
                    <w:qFormat/>
                    <w:rsid w:val="00E60125"/>
                    <w:pPr>
                        <w:widowControl w:val="0"/>
                        <w:spacing w:line="300" w:lineRule="auto"/>
                        <w:jc w:val="both"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:cs="Calibri"/>
                        <w:kern w:val="2"/>
                        <w:sz w:val="21"/>
                        <w:szCs w:val="21"/>
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
                    <w:qFormat/>
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
                <w:style w:type="paragraph" w:customStyle="1" w:styleId="-">
                    <w:name w:val="正文-认证"/>
                    <w:basedOn w:val="a"/>
                    <w:uiPriority w:val="99"/>
                    <w:rsid w:val="00E60125"/>
                    <w:pPr>
                        <w:tabs>
                            <w:tab w:val="left" w:pos="5103"/>
                        </w:tabs>
                        <w:autoSpaceDE w:val="0"/>
                        <w:autoSpaceDN w:val="0"/>
                        <w:ind w:firstLine="480"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                        <w:color w:val="000000"/>
                        <w:sz w:val="24"/>
                        <w:szCs w:val="24"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="a3">
                    <w:name w:val="List Paragraph"/>
                    <w:basedOn w:val="a"/>
                    <w:qFormat/>
                    <w:rsid w:val="00E60125"/>
                    <w:pPr>
                        <w:widowControl/>
                        <w:spacing w:after="200" w:line="276" w:lineRule="auto"/>
                        <w:ind w:firstLineChars="200" w:firstLine="420"/>
                        <w:jc w:val="left"/>
                    </w:pPr>
                    <w:rPr>
                        <w:kern w:val="0"/>
                        <w:sz w:val="22"/>
                        <w:szCs w:val="22"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:customStyle="1" w:styleId="B">
                    <w:name w:val="B级标题"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="BChar1"/>
                    <w:rsid w:val="005A78F1"/>
                    <w:pPr>
                        <w:spacing w:before="156" w:after="156" w:line="240" w:lineRule="atLeast"/>
                        <w:ind w:firstLineChars="200" w:firstLine="482"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="Times New Roman"/>
                        <w:b/>
                        <w:bCs/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="24"/>
                        <w:szCs w:val="24"/>
                        <w:lang w:val="x-none" w:eastAsia="x-none"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:customStyle="1" w:styleId="a4">
                    <w:name w:val="题头"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="Char"/>
                    <w:uiPriority w:val="99"/>
                    <w:rsid w:val="005A78F1"/>
                    <w:pPr>
                        <w:spacing w:line="400" w:lineRule="exact"/>
                        <w:ind w:firstLineChars="200" w:firstLine="200"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体" w:cs="Times New Roman"/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="24"/>
                        <w:szCs w:val="24"/>
                        <w:lang w:val="x-none" w:eastAsia="x-none"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:customStyle="1" w:styleId="C">
                    <w:name w:val="C级标题"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="CChar"/>
                    <w:uiPriority w:val="99"/>
                    <w:rsid w:val="005A78F1"/>
                    <w:pPr>
                        <w:ind w:firstLineChars="200" w:firstLine="422"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                        <w:b/>
                        <w:bCs/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="20"/>
                        <w:szCs w:val="20"/>
                        <w:lang w:val="x-none" w:eastAsia="x-none"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:customStyle="1" w:styleId="a5">
                    <w:name w:val="主正文"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="Char0"/>
                    <w:uiPriority w:val="99"/>
                    <w:rsid w:val="005A78F1"/>
                    <w:pPr>
                        <w:ind w:firstLineChars="200" w:firstLine="420"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                        <w:kern w:val="0"/>
                        <w:sz w:val="20"/>
                        <w:szCs w:val="20"/>
                        <w:lang w:val="x-none" w:eastAsia="x-none"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="BChar1">
                    <w:name w:val="B级标题 Char1"/>
                    <w:link w:val="B"/>
                    <w:locked/>
                    <w:rsid w:val="005A78F1"/>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                        <w:b/>
                        <w:bCs/>
                        <w:sz w:val="24"/>
                        <w:szCs w:val="24"/>
                        <w:lang w:val="x-none" w:eastAsia="x-none"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="Char">
                    <w:name w:val="题头 Char"/>
                    <w:link w:val="a4"/>
                    <w:uiPriority w:val="99"/>
                    <w:locked/>
                    <w:rsid w:val="005A78F1"/>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体" w:hAnsi="宋体"/>
                        <w:sz w:val="24"/>
                        <w:szCs w:val="24"/>
                        <w:lang w:val="x-none" w:eastAsia="x-none"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="CChar">
                    <w:name w:val="C级标题 Char"/>
                    <w:link w:val="C"/>
                    <w:uiPriority w:val="99"/>
                    <w:locked/>
                    <w:rsid w:val="005A78F1"/>
                    <w:rPr>
                        <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                        <w:b/>
                        <w:bCs/>
                        <w:lang w:val="x-none" w:eastAsia="x-none"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="Char0">
                    <w:name w:val="主正文 Char"/>
                    <w:link w:val="a5"/>
                    <w:uiPriority w:val="99"/>
                    <w:locked/>
                    <w:rsid w:val="005A78F1"/>
                    <w:rPr>
                        <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                        <w:lang w:val="x-none" w:eastAsia="x-none"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="a6">
                    <w:name w:val="header"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="Char1"/>
                    <w:uiPriority w:val="99"/>
                    <w:unhideWhenUsed/>
                    <w:rsid w:val="00287A0B"/>
                    <w:pPr>
                        <w:pBdr>
                            <w:bottom w:val="single" w:sz="6" w:space="1" w:color="auto"/>
                        </w:pBdr>
                        <w:tabs>
                            <w:tab w:val="center" w:pos="4153"/>
                            <w:tab w:val="right" w:pos="8306"/>
                        </w:tabs>
                        <w:snapToGrid w:val="0"/>
                        <w:spacing w:line="240" w:lineRule="auto"/>
                        <w:jc w:val="center"/>
                    </w:pPr>
                    <w:rPr>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="Char1">
                    <w:name w:val="页眉 Char"/>
                    <w:link w:val="a6"/>
                    <w:uiPriority w:val="99"/>
                    <w:rsid w:val="00287A0B"/>
                    <w:rPr>
                        <w:rFonts w:cs="Calibri"/>
                        <w:kern w:val="2"/>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="a7">
                    <w:name w:val="footer"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="Char2"/>
                    <w:uiPriority w:val="99"/>
                    <w:unhideWhenUsed/>
                    <w:rsid w:val="00287A0B"/>
                    <w:pPr>
                        <w:tabs>
                            <w:tab w:val="center" w:pos="4153"/>
                            <w:tab w:val="right" w:pos="8306"/>
                        </w:tabs>
                        <w:snapToGrid w:val="0"/>
                        <w:spacing w:line="240" w:lineRule="auto"/>
                        <w:jc w:val="left"/>
                    </w:pPr>
                    <w:rPr>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="Char2">
                    <w:name w:val="页脚 Char"/>
                    <w:link w:val="a7"/>
                    <w:uiPriority w:val="99"/>
                    <w:rsid w:val="00287A0B"/>
                    <w:rPr>
                        <w:rFonts w:cs="Calibri"/>
                        <w:kern w:val="2"/>
                        <w:sz w:val="18"/>
                        <w:szCs w:val="18"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:customStyle="1" w:styleId="A8">
                    <w:name w:val="A级标题"/>
                    <w:basedOn w:val="a"/>
                    <w:rsid w:val="000065E8"/>
                    <w:pPr>
                        <w:spacing w:before="156" w:after="156"/>
                        <w:jc w:val="center"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                        <w:b/>
                        <w:bCs/>
                        <w:sz w:val="32"/>
                        <w:szCs w:val="32"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:customStyle="1" w:styleId="Char10">
                    <w:name w:val=" Char1"/>
                    <w:basedOn w:val="a9"/>
                    <w:rsid w:val="000065E8"/>
                    <w:pPr>
                        <w:adjustRightInd w:val="0"/>
                        <w:spacing w:line="436" w:lineRule="exact"/>
                        <w:ind w:left="357"/>
                        <w:jc w:val="left"/>
                        <w:outlineLvl w:val="3"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                        <w:szCs w:val="20"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="a9">
                    <w:name w:val="Document Map"/>
                    <w:basedOn w:val="a"/>
                    <w:semiHidden/>
                    <w:rsid w:val="000065E8"/>
                    <w:pPr>
                        <w:shd w:val="clear" w:color="auto" w:fill="000080"/>
                    </w:pPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="Heading1Char">
                    <w:name w:val="Heading 1 Char"/>
                    <w:locked/>
                    <w:rsid w:val="002E5D6B"/>
                    <w:rPr>
                        <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman"/>
                        <w:b/>
                        <w:bCs/>
                        <w:kern w:val="44"/>
                        <w:sz w:val="44"/>
                        <w:szCs w:val="44"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="table" w:styleId="aa">
                    <w:name w:val="Table Grid"/>
                    <w:basedOn w:val="a1"/>
                    <w:rsid w:val="002E5D6B"/>
                    <w:pPr>
                        <w:widowControl w:val="0"/>
                        <w:spacing w:line="300" w:lineRule="auto"/>
                        <w:jc w:val="both"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman"/>
                    </w:rPr>
                    <w:tblPr>
                        <w:tblBorders>
                            <w:top w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            <w:left w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            <w:bottom w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            <w:right w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            <w:insideH w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                            <w:insideV w:val="single" w:sz="4" w:space="0" w:color="auto"/>
                        </w:tblBorders>
                    </w:tblPr>
                </w:style>
                <w:style w:type="paragraph" w:customStyle="1" w:styleId="ab">
                    <w:name w:val="主正文二"/>
                    <w:basedOn w:val="a"/>
                    <w:rsid w:val="00635407"/>
                    <w:pPr>
                        <w:spacing w:line="240" w:lineRule="auto"/>
                        <w:ind w:firstLineChars="346" w:firstLine="727"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="宋体"/>
                        <w:szCs w:val="20"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="Char3">
                    <w:name w:val="纯文本 Char"/>
                    <w:link w:val="ac"/>
                    <w:rsid w:val="00CF1355"/>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体" w:hAnsi="Courier New" w:cs="Courier New"/>
                        <w:kern w:val="2"/>
                        <w:sz w:val="21"/>
                        <w:szCs w:val="21"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="paragraph" w:styleId="ac">
                    <w:name w:val="Plain Text"/>
                    <w:basedOn w:val="a"/>
                    <w:link w:val="Char3"/>
                    <w:rsid w:val="00CF1355"/>
                    <w:pPr>
                        <w:spacing w:line="240" w:lineRule="auto"/>
                    </w:pPr>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体" w:hAnsi="Courier New" w:cs="Times New Roman"/>
                        <w:lang w:val="x-none" w:eastAsia="x-none"/>
                    </w:rPr>
                </w:style>
                <w:style w:type="character" w:customStyle="1" w:styleId="Char11">
                    <w:name w:val="纯文本 Char1"/>
                    <w:link w:val="ac"/>
                    <w:uiPriority w:val="99"/>
                    <w:semiHidden/>
                    <w:rsid w:val="00CF1355"/>
                    <w:rPr>
                        <w:rFonts w:ascii="宋体" w:hAnsi="Courier New" w:cs="Courier New"/>
                        <w:kern w:val="2"/>
                        <w:sz w:val="21"/>
                        <w:szCs w:val="21"/>
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
                    <w:nsid w:val="05E26CC9"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="5736115C"/>
                    <w:lvl w:ilvl="0" w:tplc="08B0BFBA">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="[%1]"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="4200" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="1" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="19DF2C98"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="B39E3008"/>
                    <w:lvl w:ilvl="0" w:tplc="FAD4402E">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%1．"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="780" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="4200" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="2" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="216A1DEF"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="6A48A6A2"/>
                    <w:lvl w:ilvl="0" w:tplc="597E88CC">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%1．"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="780" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="4200" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="3" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="220B2AFD"/>
                    <w:multiLevelType w:val="multilevel"/>
                    <w:tmpl w:val="220B2AFD"/>
                    <w:lvl w:ilvl="0">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="japaneseCounting"/>
                        <w:lvlText w:val="%1、"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="810" w:hanging="450"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1200" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="1620" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2040" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2460" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="2880" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3300" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3720" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="4140" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="4" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="220F5BF2"/>
                    <w:multiLevelType w:val="multilevel"/>
                    <w:tmpl w:val="220F5BF2"/>
                    <w:lvl w:ilvl="0">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="[%1]"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="eastAsia"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="4200" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="5" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="2F476685"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="B5784076"/>
                    <w:lvl w:ilvl="0" w:tplc="04090001">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val=""/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="840"/>
                            </w:tabs>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Wingdings" w:hAnsi="Wingdings" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090003" w:tentative="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="bullet"/>
                        <w:lvlText w:val=""/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1260"/>
                            </w:tabs>
                            <w:ind w:left="1260" w:hanging="420"/>
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
                            <w:tabs>
                                <w:tab w:val="num" w:pos="1680"/>
                            </w:tabs>
                            <w:ind w:left="1680" w:hanging="420"/>
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
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2100"/>
                            </w:tabs>
                            <w:ind w:left="2100" w:hanging="420"/>
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
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2520"/>
                            </w:tabs>
                            <w:ind w:left="2520" w:hanging="420"/>
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
                            <w:tabs>
                                <w:tab w:val="num" w:pos="2940"/>
                            </w:tabs>
                            <w:ind w:left="2940" w:hanging="420"/>
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
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3360"/>
                            </w:tabs>
                            <w:ind w:left="3360" w:hanging="420"/>
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
                            <w:tabs>
                                <w:tab w:val="num" w:pos="3780"/>
                            </w:tabs>
                            <w:ind w:left="3780" w:hanging="420"/>
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
                            <w:tabs>
                                <w:tab w:val="num" w:pos="4200"/>
                            </w:tabs>
                            <w:ind w:left="4200" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Wingdings" w:hAnsi="Wingdings" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="6" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="32714FE9"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="5736115C"/>
                    <w:lvl w:ilvl="0" w:tplc="08B0BFBA">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="[%1]"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:ascii="Times New Roman" w:hAnsi="Times New Roman" w:cs="Times New Roman" w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="4200" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="7" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="497252D2"/>
                    <w:multiLevelType w:val="multilevel"/>
                    <w:tmpl w:val="497252D2"/>
                    <w:lvl w:ilvl="0">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="[%1]"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="840" w:hanging="420"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="eastAsia"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="4200" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="8" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="555C72FF"/>
                    <w:multiLevelType w:val="singleLevel"/>
                    <w:tmpl w:val="555C72FF"/>
                    <w:lvl w:ilvl="0">
                        <w:start w:val="3"/>
                        <w:numFmt w:val="chineseCounting"/>
                        <w:suff w:val="nothing"/>
                        <w:lvlText w:val="%1、"/>
                        <w:lvlJc w:val="left"/>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="9" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="71C1527D"/>
                    <w:multiLevelType w:val="hybridMultilevel"/>
                    <w:tmpl w:val="AC9C8FFA"/>
                    <w:lvl w:ilvl="0" w:tplc="B0786BC8">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="none"/>
                        <w:lvlText w:val="一、"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="872" w:hanging="450"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1262" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="1682" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3" w:tplc="0409000F">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2102" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2522" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="2942" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6" w:tplc="0409000F">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3362" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7" w:tplc="04090019">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3782" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8" w:tplc="0409001B">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="4202" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:abstractNum w:abstractNumId="10" w15:restartNumberingAfterBreak="0">
                    <w:nsid w:val="74070DAB"/>
                    <w:multiLevelType w:val="multilevel"/>
                    <w:tmpl w:val="74070DAB"/>
                    <w:lvl w:ilvl="0">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%1．"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="780" w:hanging="360"/>
                        </w:pPr>
                        <w:rPr>
                            <w:rFonts w:hint="default"/>
                        </w:rPr>
                    </w:lvl>
                    <w:lvl w:ilvl="1">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%2)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="1260" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="2">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%3."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="1680" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="3">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%4."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2100" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="4">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%5)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="2520" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="5">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%6."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="2940" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="6">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="decimal"/>
                        <w:lvlText w:val="%7."/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3360" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="7">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerLetter"/>
                        <w:lvlText w:val="%8)"/>
                        <w:lvlJc w:val="left"/>
                        <w:pPr>
                            <w:ind w:left="3780" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                    <w:lvl w:ilvl="8">
                        <w:start w:val="1"/>
                        <w:numFmt w:val="lowerRoman"/>
                        <w:lvlText w:val="%9."/>
                        <w:lvlJc w:val="right"/>
                        <w:pPr>
                            <w:ind w:left="4200" w:hanging="420"/>
                        </w:pPr>
                    </w:lvl>
                </w:abstractNum>
                <w:num w:numId="1">
                    <w:abstractNumId w:val="0"/>
                </w:num>
                <w:num w:numId="2">
                    <w:abstractNumId w:val="6"/>
                </w:num>
                <w:num w:numId="3">
                    <w:abstractNumId w:val="4"/>
                </w:num>
                <w:num w:numId="4">
                    <w:abstractNumId w:val="7"/>
                </w:num>
                <w:num w:numId="5">
                    <w:abstractNumId w:val="9"/>
                </w:num>
                <w:num w:numId="6">
                    <w:abstractNumId w:val="2"/>
                </w:num>
                <w:num w:numId="7">
                    <w:abstractNumId w:val="1"/>
                </w:num>
                <w:num w:numId="8">
                    <w:abstractNumId w:val="3"/>
                </w:num>
                <w:num w:numId="9">
                    <w:abstractNumId w:val="10"/>
                </w:num>
                <w:num w:numId="10">
                    <w:abstractNumId w:val="8"/>
                </w:num>
                <w:num w:numId="11">
                    <w:abstractNumId w:val="5"/>
                </w:num>
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
                <dc:title>附件1 ——无课内实践环节的课论课程</dc:title>
                <dc:subject/>
                <dc:creator>路胜利</dc:creator>
                <cp:keywords/>
                <cp:lastModifiedBy>余海伦</cp:lastModifiedBy>
                <cp:revision>2</cp:revision>
                <dcterms:created xsi:type="dcterms:W3CDTF">2017-09-07T07:31:00Z</dcterms:created>
                <dcterms:modified xsi:type="dcterms:W3CDTF">2017-09-07T07:31:00Z</dcterms:modified>
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
                    <w:sig w:usb0="E0002AFF" w:usb1="C000247B" w:usb2="00000009" w:usb3="00000000" w:csb0="000001FF" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="宋体">
                    <w:altName w:val="SimSun"/>
                    <w:panose1 w:val="02010600030101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="00000003" w:usb1="288F0000" w:usb2="00000016" w:usb3="00000000" w:csb0="00040001" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="Courier New">
                    <w:panose1 w:val="02070309020205020404"/>
                    <w:charset w:val="00"/>
                    <w:family w:val="modern"/>
                    <w:pitch w:val="fixed"/>
                    <w:sig w:usb0="E0002EFF" w:usb1="C0007843" w:usb2="00000009" w:usb3="00000000" w:csb0="000001FF" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="等线 Light">
                    <w:panose1 w:val="02010600030101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="A00002BF" w:usb1="38CF7CFA" w:usb2="00000016" w:usb3="00000000" w:csb0="0004000F" w:csb1="00000000"/>
                </w:font>
                <w:font w:name="等线">
                    <w:altName w:val="DengXian"/>
                    <w:panose1 w:val="02010600030101010101"/>
                    <w:charset w:val="86"/>
                    <w:family w:val="auto"/>
                    <w:pitch w:val="variable"/>
                    <w:sig w:usb0="A00002BF" w:usb1="38CF7CFA" w:usb2="00000016" w:usb3="00000000" w:csb0="0004000F" w:csb1="00000000"/>
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
                <TotalTime>1</TotalTime>
                <Pages>3</Pages>
                <Words>305</Words>
                <Characters>1740</Characters>
                <Application>Microsoft Office Word</Application>
                <DocSecurity>0</DocSecurity>
                <Lines>14</Lines>
                <Paragraphs>4</Paragraphs>
                <ScaleCrop>false</ScaleCrop>
                <Company>China</Company>
                <LinksUpToDate>false</LinksUpToDate>
                <CharactersWithSpaces>2041</CharactersWithSpaces>
                <SharedDoc>false</SharedDoc>
                <HyperlinksChanged>false</HyperlinksChanged>
                <AppVersion>16.0000</AppVersion>
            </Properties>
        </pkg:xmlData>
    </pkg:part>
</pkg:package>